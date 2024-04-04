@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.mifos.feature.client.clientDetails.presentation

import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.MobileFriendly
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mifos.core.common.utils.Utils
import com.mifos.core.designsystem.component.MifosCircularProgress
import com.mifos.core.designsystem.component.MifosClientDetailsText
import com.mifos.core.designsystem.component.MifosMenuDropDownItem
import com.mifos.core.designsystem.component.MifosSweetError
import com.mifos.core.designsystem.theme.Black
import com.mifos.core.designsystem.theme.BluePrimary
import com.mifos.core.designsystem.theme.BluePrimaryDark
import com.mifos.core.designsystem.theme.BlueSecondary
import com.mifos.core.designsystem.theme.DarkGray
import com.mifos.core.designsystem.theme.White
import com.mifos.core.objects.accounts.loan.LoanAccount
import com.mifos.core.objects.accounts.savings.DepositType
import com.mifos.core.objects.accounts.savings.SavingsAccount
import com.mifos.core.objects.client.Client
import com.mifos.feature.client.R
import java.io.File
import java.util.Objects

/**
 * Created by Aditya Gupta on 18/03/24.
 */

@Composable
fun ClientDetailsScreen(
    clientDetailsViewModel: ClientDetailsViewModel = hiltViewModel(),
    clientId: Int,
    onBackPressed: () -> Unit,
    addLoanAccount: (Int) -> Unit,
    addSavingsAccount: (Int) -> Unit,
    charges: (Int) -> Unit,
    documents: (Int) -> Unit,
    identifiers: (Int) -> Unit,
    moreClientInfo: (Int) -> Unit,
    notes: (Int) -> Unit,
    pinpointLocation: (Int) -> Unit,
    survey: (Int) -> Unit,
    uploadSignature: (Int) -> Unit,
    loanAccountSelected: (Int) -> Unit,
    savingsAccountSelected: (Int, DepositType) -> Unit,
    activateClient: (Int) -> Unit
) {

    val context = LocalContext.current
    val state = clientDetailsViewModel.clientDetailsUiState.collectAsStateWithLifecycle().value
    val client = rememberSaveable { mutableStateOf<Client?>(null) }
    val loanAccounts = rememberSaveable { mutableStateOf<List<LoanAccount>?>(null) }
    val savingsAccounts = rememberSaveable { mutableStateOf<List<SavingsAccount>?>(null) }
    var clientNotFoundError by rememberSaveable { mutableStateOf(false) }
    var showLoading by rememberSaveable { mutableStateOf(true) }

    var showMenu by remember { mutableStateOf(false) }
    val showSelectImageDialog = remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    val file = context.createImageFile()
    val cameraImageUri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.mifos.mifosxdroid" + ".provider",
        file
    )
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA
        )
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
                val bitmap = context.contentResolver.openInputStream(uri).use { stream ->
                    BitmapFactory.decodeStream(stream).asImageBitmap().asAndroidBitmap()
                }
                showSelectImageDialog.value = false
                clientDetailsViewModel.saveClientImage(clientId, bitmap)
            }
        }
    )


    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { status ->
            if (status) {
                imageUri = cameraImageUri
                val bitmap = context.contentResolver.openInputStream(cameraImageUri).use { stream ->
                    BitmapFactory.decodeStream(stream).asImageBitmap().asAndroidBitmap()
                }
                showSelectImageDialog.value = false
                clientDetailsViewModel.saveClientImage(clientId, bitmap)
            }
        }

    LaunchedEffect(key1 = true) {
        clientDetailsViewModel.loadClientDetailsAndClientAccounts(clientId)
    }

    when (state) {
        is ClientDetailsUiState.Loading -> {
            showLoading = true
        }

        is ClientDetailsUiState.ShowClientImageDeletedSuccessfully -> {
            LaunchedEffect(key1 = true) {
                snackbarHostState.showSnackbar(message = "Client Image Deleted Successfully")
                showLoading = false
            }
        }

        is ClientDetailsUiState.ShowClientDetails -> {
            client.value = state.client
            loanAccounts.value = state.clientAccounts?.loanAccounts
            savingsAccounts.value = state.clientAccounts?.savingsAccounts
            showLoading = false
        }

        is ClientDetailsUiState.ShowUploadImageSuccessfully -> {
            LaunchedEffect(key1 = state.response) {
                snackbarHostState.showSnackbar(message = "Client Image Uploaded successfully")
                showLoading = false
            }
        }

        is ClientDetailsUiState.ShowError -> {
            when (state.message == "null") {
                true -> {
                    clientNotFoundError = true
                    showLoading = false
                }

                false -> {
                    LaunchedEffect(key1 = state.message) {
                        snackbarHostState.showSnackbar(message = state.message)
                        showLoading = false
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = White),
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = null,
                            tint = Black,
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.feature_client),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily(Font(R.font.outfit_medium))
                        ),
                        color = Black,
                        textAlign = TextAlign.Start
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        modifier = Modifier.background(White),
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        MifosMenuDropDownItem(option = stringResource(id = R.string.add_loan_account)) {
                            addLoanAccount(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.add_savings_account)) {
                            addSavingsAccount(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.charges)) {
                            charges(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.documents)) {
                            documents(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.identifiers)) {
                            identifiers(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.more_client_info)) {
                            moreClientInfo(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.notes)) {
                            notes(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.pinpoint_location)) {
                            pinpointLocation(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.survey)) {
                            survey(clientId)
                            showMenu = false
                        }
                        MifosMenuDropDownItem(option = stringResource(id = R.string.upload_signature)) {
                            uploadSignature(clientId)
                            showMenu = false
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = White,
        bottomBar = {
            if (client.value?.active == false) {
                Button(
                    onClick = { activateClient(clientId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(44.dp)
                        .padding(start = 16.dp, end = 16.dp),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) BluePrimaryDark else BluePrimary
                    )
                ) {
                    Text(text = "Activate Client", fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        if (showSelectImageDialog.value) {
            MifosSelectImageDialog(showSelectImageDialog,
                takeImage = {
                    permissionState.permissions.forEach { per ->
                        when (per.permission) {
                            Manifest.permission.CAMERA -> {
                                when {
                                    per.status.isGranted -> {
                                        cameraLauncher.launch(cameraImageUri)
                                    }

                                    else -> {
                                        permissionState.launchMultiplePermissionRequest()
                                    }
                                }
                            }
                        }
                    }
                },
                uploadImage = {
                    galleryLauncher.launch("image/*")
                }, deleteImage = {
                    clientDetailsViewModel.deleteClientImage(clientId)
                    showSelectImageDialog.value = false
                })
        }
        if (clientNotFoundError) {
            MifosSweetError(message = "Client Not Found") {

            }
        } else {
            if (showLoading) {
                MifosCircularProgress()
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(75.dp)
                                .clip(RoundedCornerShape(100))
                                .clickable(onClick = {
                                    showSelectImageDialog.value = true
                                }),
                            model = if (client.value?.imagePresent == true) {
                                client.value?.clientId?.let {
                                    LaunchedEffect(key1 = it) {
                                        clientDetailsViewModel.getClientImageUrl(
                                            it
                                        )
                                    }
                                }
                            } else R.drawable.ic_launcher,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    client.value?.displayName?.let {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = it,
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Normal,
                                fontFamily = FontFamily(Font(R.font.outfit_regular))
                            ),
                            color = Black,
                            textAlign = TextAlign.Start
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    client.value?.accountNo?.let {
                        MifosClientDetailsText(
                            icon = Icons.Outlined.Numbers,
                            field = stringResource(id = R.string.account_number),
                            value = it
                        )
                    }
                    client.value?.externalId?.let {
                        MifosClientDetailsText(
                            icon = Icons.Outlined.Numbers,
                            field = stringResource(id = R.string.external_id),
                            value = it
                        )
                    }
                    client.value?.let { Utils.getStringOfDate(it.activationDate) }?.let {
                        MifosClientDetailsText(
                            icon = Icons.Outlined.DateRange,
                            field = stringResource(id = R.string.activation_date),
                            value = it
                        )
                    }
                    client.value?.officeName?.let {
                        MifosClientDetailsText(
                            icon = Icons.Outlined.HomeWork,
                            field = stringResource(id = R.string.office),
                            value = it
                        )
                    }
                    client.value?.mobileNo?.let {
                        MifosClientDetailsText(
                            icon = Icons.Outlined.MobileFriendly,
                            field = stringResource(id = R.string.mobile_no),
                            value = it
                        )
                    }
                    client.value?.groupNames?.let {
                        MifosClientDetailsText(
                            icon = Icons.Outlined.Groups,
                            field = stringResource(id = R.string.group),
                            value = it
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    if (loanAccounts.value != null && savingsAccounts.value != null) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp),
                            text = stringResource(id = R.string.accounts),
                            style = TextStyle(
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Normal,
                                fontFamily = FontFamily(Font(R.font.outfit_medium))
                            ),
                            color = Black,
                            textAlign = TextAlign.Start
                        )
                    }
                    loanAccounts.value?.let {
                        MifosLoanAccountExpendableCard(
                            stringResource(id = R.string.loan_account),
                            it,
                            loanAccountSelected
                        )
                    }
                    savingsAccounts.value?.let {
                        MifosSavingsAccountExpendableCard(
                            stringResource(id = R.string.savings_account),
                            it, savingsAccountSelected
                        )
                    }
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    val imageFileName = "client_image"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

@Composable
fun MifosLoanAccountExpendableCard(
    accountType: String,
    loanAccounts: List<LoanAccount>,
    loanAccountSelected: (Int) -> Unit
) {

    var expendableState by remember { mutableStateOf(false) }
    val rotateState by animateFloatAsState(
        targetValue = if (expendableState) 180f else 0f,
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(BlueSecondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    text = accountType,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_regular))
                    ),
                    color = Black,
                    textAlign = TextAlign.Start
                )
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = { expendableState = !expendableState }) {
                    Icon(
                        modifier = Modifier.rotate(rotateState),
                        imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null
                    )
                }
            }

            if (expendableState) {

                Spacer(modifier = Modifier.height(10.dp))
                MifosLoanAccountsLazyColumn(loanAccounts, loanAccountSelected)
            }
        }
    }
}


@Composable
fun MifosLoanAccountsLazyColumn(
    loanAccounts: List<LoanAccount>,
    loanAccountSelected: (Int) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(White)
    ) {
        LazyColumn(
            modifier = Modifier
                .height((loanAccounts.size * 52).dp)
                .padding(6.dp)
        ) {
            items(loanAccounts) { loanAccount ->
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable(onClick = {
                            loanAccount.id?.let {
                                loanAccountSelected(
                                    it
                                )
                            }
                        }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp), onDraw = {
                        drawCircle(
                            color = when {
                                loanAccount.status?.active == true -> {
                                    Color.Green
                                }

                                loanAccount.status?.waitingForDisbursal == true -> {
                                    Color.Blue
                                }

                                loanAccount.status?.pendingApproval == true -> {
                                    Color.Yellow
                                }

                                loanAccount.status?.active == true && loanAccount.inArrears == true -> {
                                    Color.Red
                                }

                                else -> {
                                    Color.DarkGray
                                }
                            }
                        )
                    })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        loanAccount.productName?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_regular))
                                ),
                                color = Black,
                                textAlign = TextAlign.Start
                            )
                        }
                        Text(
                            text = loanAccount.accountNo.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Normal,
                                fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_light))
                            ),
                            color = DarkGray,
                            textAlign = TextAlign.Start
                        )
                    }
                    loanAccount.productId?.let {
                        Text(
                            text = it.toString(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Normal,
                                fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_regular))
                            ),
                            color = Black,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MifosSavingsAccountExpendableCard(
    accountType: String,
    savingsAccount: List<SavingsAccount>,
    savingsAccountSelected: (Int, DepositType) -> Unit
) {

    var expendableState by remember { mutableStateOf(false) }
    val rotateState by animateFloatAsState(
        targetValue = if (expendableState) 180f else 0f,
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(BlueSecondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    text = accountType,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_regular))
                    ),
                    color = Black,
                    textAlign = TextAlign.Start
                )
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = { expendableState = !expendableState }) {
                    Icon(
                        modifier = Modifier.rotate(rotateState),
                        imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null
                    )
                }
            }

            if (expendableState) {

                Spacer(modifier = Modifier.height(10.dp))
                MifosSavingsAccountsLazyColumn(savingsAccount, savingsAccountSelected)
            }
        }
    }
}


@Composable
fun MifosSavingsAccountsLazyColumn(
    savingsAccounts: List<SavingsAccount>,
    savingsAccountSelected: (Int, DepositType) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(White)
    ) {
        LazyColumn(
            modifier = Modifier
                .height((savingsAccounts.size * 50).dp)
                .padding(6.dp)
        ) {
            items(savingsAccounts) { savingsAccount ->
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable(onClick = {
                            savingsAccount.id?.let {
                                savingsAccount.depositType?.let { it1 ->
                                    savingsAccountSelected(
                                        it, it1
                                    )
                                }
                            }
                        }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp), onDraw = {
                        drawCircle(
                            color = when {
                                savingsAccount.status?.active == true -> {
                                    Color.Green
                                }

                                savingsAccount.status?.approved == true -> {
                                    Color.Blue
                                }

                                savingsAccount.status?.submittedAndPendingApproval == true -> {
                                    Color.Yellow
                                }

                                else -> {
                                    Color.DarkGray
                                }
                            }
                        )
                    })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        savingsAccount.productName?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_regular))
                                ),
                                color = Black,
                                textAlign = TextAlign.Start
                            )
                        }
                        Text(
                            text = savingsAccount.accountNo.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Normal,
                                fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_light))
                            ),
                            color = DarkGray,
                            textAlign = TextAlign.Start
                        )
                    }
                    savingsAccount.productId?.let {
                        Text(
                            text = it.toString(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Normal,
                                fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_regular))
                            ),
                            color = Black,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MifosSelectImageDialog(
    showSelectImageDialog: MutableState<Boolean>,
    takeImage: () -> Unit,
    uploadImage: () -> Unit,
    deleteImage: () -> Unit
) {

    Dialog(
        onDismissRequest = { showSelectImageDialog.value = !showSelectImageDialog.value },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            colors = CardDefaults.cardColors(White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Please Select", modifier = Modifier.fillMaxWidth(), style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_medium))
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { takeImage() },
                    colors = ButtonDefaults.buttonColors(BlueSecondary)
                ) {
                    Text(
                        text = "Take new image",
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_medium))
                        ),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = { uploadImage() },
                    colors = ButtonDefaults.buttonColors(BlueSecondary)
                ) {
                    Text(
                        text = "Upload new image",
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_medium))
                        ),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = { deleteImage() },
                    colors = ButtonDefaults.buttonColors(BlueSecondary)
                ) {
                    Text(
                        text = "Delete Image",
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily(Font(com.mifos.core.designsystem.R.font.outfit_medium))
                        ),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ClientDetailsScreenPreview() {
    ClientDetailsScreen(
        clientId = 1,
        onBackPressed = {},
        addLoanAccount = {},
        addSavingsAccount = {},
        charges = {},
        documents = {},
        identifiers = {},
        moreClientInfo = {},
        notes = {},
        pinpointLocation = {},
        survey = {},
        uploadSignature = {},
        loanAccountSelected = {},
        savingsAccountSelected = { _, _ -> },
        activateClient = {}
    )
}