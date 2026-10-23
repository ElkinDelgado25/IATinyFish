# TinyPrice

Proyecto de la actividad PEA: integración de una app Android nativa con TinyFish
Search mediante una Edge Function de Supabase.

## Arquitectura

- `data/model/ProductOffer.kt`: modelo de ofertas.
- `data/repository/DealsRepository.kt`: contrato del repositorio.
- `data/repository/RemoteDealsRepository.kt`: llamada Retrofit a Supabase.
- `presentation/deals/DealsViewModel.kt`: StateFlow, eventos, carga y errores.
- `MainActivity.kt`: interfaz Jetpack Compose.
- `supabase/functions/search-products/index.ts`: validación, secreto y transformación.

## Seguridad

`TINYFISH_API_KEY` debe existir únicamente como secreto alojado en Supabase.
No se debe guardar en Kotlin, BuildConfig, `local.properties`, GitHub ni capturas.

## Compilación

```bash
./gradlew assembleDebug
```

## Despliegue pendiente

El proyecto Supabase configurado es `qbbteihjjhvegqdvfqrh`. El secreto se
configura sin escribirlo en el repositorio:

```bash
supabase secrets set TINYFISH_API_KEY='clave-leída-de-forma-segura'
supabase functions deploy search-products
```
