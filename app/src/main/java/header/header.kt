package header

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import languagesBack.getStringByName
import menuHamburguesa.CustomPopupMenu
import model.Quiz
import quizcraft.fetchTopTags
import quizcraft.searchQuizzesByTag
import routes.NavigationActions
import routes.Routes
import uiPrincipal.SharedState
import uiUserInfo.userInfoBack

var quizzes = mutableStateOf<List<Quiz>>(emptyList())


@Composable
fun getHeader(
    navigationActions: NavigationActions,
    navController: NavHostController,
    viewModelUser: userInfoBack = viewModel()
) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val context = LocalContext.current
    var expanded by remember {
        mutableStateOf(false)
    }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (!SharedState.isSearchActive || currentRoute != Routes.HOME) {
            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.non_registered_account_icon),
                    contentDescription = "Imagen De ejemplo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    getStringByName(context, "warning_try_enter_to_profile"),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                LaunchedEffect(Unit) {
                    viewModelUser.getInfoUser { url ->
                        profileImageUrl = url?.get("PerfilImage") as? String
                    }
                }

                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de perfil",
                    error = painterResource(id = R.drawable.perro_mordor), // Opcional, si tienes una imagen de error
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .clickable { navigationActions.navigateToUserInfo() },
                    contentScale = ContentScale.Crop
                )
            }



            Box {
                IconButton(
                    onClick = { expanded = true },
                ) {
                    Image(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "Menu",
                        modifier = Modifier.size(30.dp)
                    )
                }
                Box {
                    CustomPopupMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        color = 0xFF212325.toInt(),
                        size = if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) 175 else 150,
                        type = "header",
                        navigationActions = navigationActions,
                        quizId = ""
                    )
                }

            }
        } else {
            var searchText by remember { mutableStateOf("") }
            val debounceJob = remember { mutableStateOf<Job?>(null) }
            val suggestions = remember { mutableStateOf(emptyList<Pair<String, Int>>()) }


            Box(
                modifier = Modifier
                    .border(
                        2.dp,
                        Color(0xFFC49450),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Column {
                    TextField(
                        value = searchText,
                        onValueChange = { newText ->
                            searchText = newText
                            fetchTopTags() { topTags ->
                                suggestions.value = topTags
                            }
                            SharedState.isClickedSuggestion = true
                            debounceJob.value?.cancel()
                            debounceJob.value = CoroutineScope(Dispatchers.Main).launch {
                                delay(300)
                                searchQuizzesByTag(searchText, onResult = { result ->
                                    quizzes.value = result
                                })
                            }
                        },
                        label = {
                            getStringByName(
                                LocalContext.current,
                                "placeholder_search"
                            )?.let { Text(text = it) }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFFDFD),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedLabelColor = Color(0x80C49450),
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                searchQuizzesByTag(searchText, onResult = { result ->
                                    quizzes.value = result
                                })
                                SharedState.isSearchActive = false
                            }
                        )
                    )

                    if (suggestions.value.isNotEmpty()) {
                        AnimatedVisibility(
                            visible = SharedState.isClickedSuggestion,
                            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(suggestions.value) { (tag, count) ->
                                    Text(
                                        text = "$tag: $count "+getStringByName(LocalContext.current, "suggestion_search") + if (count > 1) "s" else "",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                searchText = tag
                                                searchQuizzesByTag(searchText, onResult = { result ->
                                                    quizzes.value = result
                                                })
                                                SharedState.isClickedSuggestion = !SharedState.isClickedSuggestion
                                                SharedState.isSearchActive = !SharedState.isSearchActive
                                            }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
