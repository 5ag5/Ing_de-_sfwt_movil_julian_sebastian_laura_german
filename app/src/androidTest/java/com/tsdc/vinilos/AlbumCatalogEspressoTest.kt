package com.tsdc.vinilos

import androidx.activity.ComponentActivity
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsdc.vinilos.domain.usecases.GetAlbumByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumsUseCase
import com.tsdc.vinilos.ui.screens.AlbumDetailScreen
import com.tsdc.vinilos.ui.screens.HomeScreen
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.AlbumDetailViewModel
import com.tsdc.vinilos.ui.viewmodels.AlbumViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas instrumentadas (Espresso / Compose UI Test) que cubren:
 *
 *  HU01 – Como usuario visitante quiero navegar el catálogo de los álbumes
 *          para escoger los que más me interesan.
 *
 *  HU02 – Como usuario visitante quiero ver el detalle de un álbum
 *          para ampliar la información sobre él.
 *
 * Se usa [FakeAlbumRepository] para evitar dependencia del backend en tiempo de prueba.
 */
@RunWith(AndroidJUnit4::class)
class AlbumCatalogEspressoTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    // ------------------------------------------------------------------
    // Helpers de lanzamiento
    // ------------------------------------------------------------------

    /** Lanza [HomeScreen] directamente en la pestaña de Álbumes con datos fake. */
    private fun launchCatalog(onAlbumClick: (Int) -> Unit = {}) {
        composeRule.setContent {
            VinilosTheme {
                val viewModel = remember {
                    AlbumViewModel(GetAlbumsUseCase(FakeAlbumRepository()))
                }
                HomeScreen(
                    viewModel = viewModel,
                    initialTab = 1, // Abre directamente la pestaña Albums
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }

    /** Lanza [AlbumDetailScreen] para el álbum indicado con datos fake. */
    private fun launchDetail(albumId: Int) {
        composeRule.setContent {
            VinilosTheme {
                val detailViewModel = remember {
                    AlbumDetailViewModel(GetAlbumByIdUseCase(FakeAlbumRepository()))
                }
                AlbumDetailScreen(
                    viewModel = detailViewModel,
                    albumId = albumId,
                    onBack = {}
                )
            }
        }
    }

    /** Espera a que la lista de álbumes contenga al menos un ítem. */
    private fun waitForAlbumList(timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule
                .onAllNodesWithTag(UiTestTags.ALBUM_LIST_ITEM, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    /** Espera a que el texto indicado aparezca en pantalla. */
    private fun waitForText(text: String, timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    // ------------------------------------------------------------------
    // HU01 – Navegar el catálogo de álbumes
    // ------------------------------------------------------------------

    /**
     * Test 1 – La pantalla de catálogo muestra el encabezado "YOUR LIBRARY"
     * cuando el usuario navega a la pestaña Álbumes.
     */
    @Test
    fun hu01_albumsTab_showsLibraryHeader() {
        launchCatalog()

        composeRule.onNodeWithText("YOUR LIBRARY").assertIsDisplayed()
    }

    /**
     * Test 2 – El catálogo muestra todos los ítems de álbum disponibles
     * en el repositorio (al menos 4 con los datos fake).
     */
    @Test
    fun hu01_catalogScreen_displaysAllAlbumItems() {
        launchCatalog()
        waitForAlbumList()

        val itemCount = composeRule
            .onAllNodesWithTag(UiTestTags.ALBUM_LIST_ITEM, useUnmergedTree = true)
            .fetchSemanticsNodes()
            .size

        assertTrue(
            "Se esperaban al menos 4 álbumes en el catálogo, pero se encontraron $itemCount",
            itemCount >= 4
        )
    }

    /**
     * Test 3 – El campo de búsqueda filtra los álbumes por título:
     * solo deben quedar los que coincidan con el texto ingresado.
     */
    @Test
    fun hu01_searchField_filtersCatalogByTitle() {
        launchCatalog()
        waitForAlbumList()

        // Estado inicial: álbum no relacionado con la búsqueda está visible
        waitForText("Buscando América")
        composeRule.onNodeWithText("Buscando América").assertIsDisplayed()

        // Acción: escribir en el campo de búsqueda
        composeRule
            .onNodeWithTag("album_search_field")
            .performTextReplacement("Opera")
        composeRule.waitForIdle()

        // Resultado: solo el álbum que coincide con "Opera" debe ser visible
        waitForText("A Night at the Opera")
        composeRule.onNodeWithText("A Night at the Opera").assertIsDisplayed()

        // El álbum que no coincide ya no debe estar en pantalla
        composeRule.waitUntil(5_000L) {
            composeRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes()
                .isEmpty()
        }
    }

    // ------------------------------------------------------------------
    // HU02 – Ver el detalle de un álbum
    // ------------------------------------------------------------------

    /**
     * Test 4 – Al abrir un álbum se presenta la pantalla de detalle
     * con el encabezado "Detalle del álbum" y el contenedor raíz.
     */
    @Test
    fun hu02_albumDetail_showsDetailHeader() {
        launchDetail(albumId = 1)

        composeRule.onNodeWithText("Detalle del álbum").assertIsDisplayed()
        composeRule
            .onNodeWithTag(UiTestTags.ALBUM_DETAIL_ROOT, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    /**
     * Test 5 – La pantalla de detalle muestra el nombre del álbum seleccionado.
     */
    @Test
    fun hu02_albumDetail_displaysAlbumName() {
        // Álbum id=2 → "Poeta del pueblo"
        launchDetail(albumId = 2)

        waitForText("Poeta del pueblo")
        composeRule.onNodeWithText("Poeta del pueblo").assertIsDisplayed()
    }

    /**
     * Test 6 – La pantalla de detalle expone los metadatos del álbum:
     * género, sello discográfico y la sección "Descripción".
     */
    @Test
    fun hu02_albumDetail_showsMetadataFields() {
        // Álbum id=3 → A Night at the Opera, Rock, EMI
        launchDetail(albumId = 3)

        waitForText("A Night at the Opera")

        // Etiquetas de los campos de metadatos
        composeRule.onNodeWithText("Género").assertIsDisplayed()
        composeRule.onNodeWithText("Sello discográfico").assertIsDisplayed()
        composeRule.onNodeWithText("Descripción").assertIsDisplayed()

        // Valores específicos del álbum
        composeRule.onNodeWithText("Rock").assertIsDisplayed()
        composeRule.onNodeWithText("EMI").assertIsDisplayed()
    }
}
