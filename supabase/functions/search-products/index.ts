import "jsr:@supabase/functions-js/edge-runtime.d.ts";

type SearchRequest = { query?: string };
const corsHeaders = {
  "access-control-allow-headers": "authorization, x-client-info, apikey, content-type",
  "access-control-allow-methods": "POST, OPTIONS",
  "access-control-allow-origin": "*",
  "content-type": "application/json",
};

Deno.serve(async (request) => {
  if (request.method === "OPTIONS") {
    return new Response("ok", { headers: corsHeaders });
  }
  if (request.method !== "POST") {
    return new Response(JSON.stringify({ error: "Method not allowed" }), {
      status: 405,
      headers: corsHeaders,
    });
  }

  const { query } = (await request.json()) as SearchRequest;
  if (!query?.trim()) {
    return new Response(JSON.stringify({ error: "query is required" }), {
      status: 400,
      headers: corsHeaders,
    });
  }

  const apiKey = Deno.env.get("TINYFISH_API_KEY");
  if (!apiKey) {
    return new Response(JSON.stringify({ error: "Server is not configured" }), {
      status: 500,
      headers: corsHeaders,
    });
  }

  const searchUrl = new URL("https://api.search.tinyfish.ai");
  searchUrl.searchParams.set("query", query.trim());
  searchUrl.searchParams.set("language", "es");
  searchUrl.searchParams.set("location", "EC");
  const response = await fetch(searchUrl, {
    headers: { "X-API-Key": apiKey },
  });
  if (!response.ok) {
    return new Response(JSON.stringify({ error: "TinyFish Search no está disponible." }), {
      status: 502,
      headers: corsHeaders,
    });
  }
  const payload = await response.json();
  const offers = (payload.results ?? []).slice(0, 10).map((result: {
    url: string;
    title: string;
    site_name?: string;
    snippet?: string;
  }, index: number) => ({
    id: `${index + 1}-${result.url}`,
    store: result.site_name ?? "Fuente web",
    productName: result.title,
    price: "Ver en fuente",
    sourceUrl: result.url,
    summary: result.snippet ?? "Resultado web",
    tags: ["Resultado web"],
  }));
  return new Response(JSON.stringify({ query: query.trim(), offers }), {
    headers: corsHeaders,
  });
});
