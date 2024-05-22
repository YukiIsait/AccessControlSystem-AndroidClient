package tech.youko.smartcard.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.youko.smartcard.R
import tech.youko.smartcard.ui.viewmodel.MaintenanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(
    viewModel: MaintenanceViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    val id by viewModel.id.collectAsState()
    val mac by viewModel.mac.collectAsState()
    val ip by viewModel.ip.collectAsState()
    val subnet by viewModel.subnet.collectAsState()
    val gateway by viewModel.gateway.collectAsState()
    val dns by viewModel.dns.collectAsState()
    val server by viewModel.server.collectAsState()
    val port by viewModel.port.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.title_app_name))
                },
                actions = {
                    IconButton(
                        onClick = viewModel::onEndMaintenance
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_close),
                            contentDescription = stringResource(R.string.title_end_maintenance)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = viewModel::saveDeviceInformation,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_save),
                        contentDescription = stringResource(R.string.title_save)
                    )
                },
                text = {
                    Text(stringResource(R.string.title_save))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(40.dp)
                .verticalScroll(scrollState)
        ) {
            TextField(
                value = id,
                onValueChange = viewModel::updateId,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_id))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))
            TextField(
                value = mac,
                onValueChange = viewModel::updateMac,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_mac))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))
            TextField(
                value = ip,
                onValueChange = viewModel::updateIp,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_ip))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))
            TextField(
                value = subnet,
                onValueChange = viewModel::updateSubnet,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_subnet))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))
            TextField(
                value = gateway,
                onValueChange = viewModel::updateGateway,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_gateway))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))
            TextField(
                value = dns,
                onValueChange = viewModel::updateDns,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_dns))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))
            TextField(
                value = server,
                onValueChange = viewModel::updateServer,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_server))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))
            TextField(
                value = port.toString(),
                onValueChange = { viewModel.updatePort(it.toInt()) },
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_port))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
