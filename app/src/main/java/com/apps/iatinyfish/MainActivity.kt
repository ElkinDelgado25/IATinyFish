package com.apps.iatinyfish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apps.iatinyfish.data.model.ProductOffer
import com.apps.iatinyfish.presentation.deals.DealsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TinyPriceApp() }
    }
}

@Composable
private fun TinyPriceApp(viewModel: DealsViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("TinyPrice", style = MaterialTheme.typography.headlineLarge)
            Text("Encuentra la mejor compra.", style = MaterialTheme.typography.bodyLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = viewModel::onQueryChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Producto") },
                    singleLine = true,
                )
                Button(onClick = viewModel::search) { Text("Buscar") }
            }
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error)
                state.offers.isEmpty() -> Text("Busca un producto para ver ofertas.")
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.offers, key = ProductOffer::id) { offer ->
                        OfferCard(offer)
                    }
                }
            }
            state.selectedOffer?.let { offer ->
                Text("Detalle: ${offer.productName} · ${offer.store}\n${offer.summary}")
            }
        }
    }
}

@Composable
private fun OfferCard(offer: ProductOffer) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(offer.store, style = MaterialTheme.typography.labelLarge)
            Text(offer.productName, style = MaterialTheme.typography.titleMedium)
            Text(offer.price)
            Text(offer.summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
