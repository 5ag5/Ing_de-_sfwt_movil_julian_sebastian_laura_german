package com.tsdc.vinilos

import androidx.activity.ComponentActivity
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsdc.vinilos.domain.usecases.GetAlbumsUseCase
import com.tsdc.vinilos.domain.usecases.GetArtistsUseCase
import com.tsdc.vinilos.domain.usecases.GetCollectorsUseCase
import com.tsdc.vinilos.ui.screens.HomeScreen
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.AlbumViewModel
import com.tsdc.vinilos.ui.viewmodels.ArtistViewModel
import com.tsdc.vinilos.ui.viewmodels.CollectorViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumScreenEspressoTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun launchHomeWithFakeCatalog() {
        composeRule.setContent {
            VinilosTheme {
                val albumViewModel = remember {
                    AlbumViewModel(GetAlbumsUseCase(FakeAlbumRepository()))
                }
                val artistViewModel = remember {
                    ArtistViewModel(GetArtistsUseCase(FakeArtistRepository()))
                }
                val collectorViewModel = remember {
                    CollectorViewModel(GetCollectorsUseCase(FakeCollectorRepository()))
                }
                HomeScreen(
                    albumViewModel = albumViewModel,
                    artistViewModel = artistViewModel,
                    collectorViewModel = collectorViewModel,
                    initialTab = 0,
                    onAlbumClick = { /* Navigate to detail */ }
                )
            }
        }
    }

    private fun navigateToAlbums() {
        composeRule.onNodeWithTag("nav_albums").performClick()
    }

    private fun assertAlbumTitleVisible(title: String, timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule.onAllNodesWithText(title).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithText(title).onFirst().assertIsDisplayed()
    }

    private fun assertAlbumTitleAbsent(title: String, timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule.onAllNodesWithText(title).fetchSemanticsNodes().isEmpty()
        }
    }

    @Test
    fun navigateToAlbums_showsLibraryHeader() {
        launchHomeWithFakeCatalog()
        navigateToAlbums()
        composeRule.onNodeWithText("YOUR LIBRARY").assertIsDisplayed()
        composeRule.onNodeWithText("The Analog Curator").assertIsDisplayed()
    }

    @Test
    fun albumsList_displaysCatalogTitles() {
        launchHomeWithFakeCatalog()
        navigateToAlbums()

        assertAlbumTitleVisible("Buscando América")
        assertAlbumTitleVisible("Poeta del pueblo")
        assertAlbumTitleVisible("A Night at the Opera")

        composeRule.onNodeWithTag("album_list").performScrollToNode(hasText("A Day at the Races"))
        assertAlbumTitleVisible("A Day at the Races")
    }

    @Test
    fun searchFiltersAlbumsByTitle() {
        launchHomeWithFakeCatalog()
        navigateToAlbums()
        assertAlbumTitleVisible("Buscando América")
        assertAlbumTitleVisible("Poeta del pueblo")

        composeRule.onNodeWithTag("album_search_field").performTextReplacement("Poeta")
        composeRule.waitForIdle()

        assertAlbumTitleVisible("Poeta del pueblo")
        assertAlbumTitleAbsent("Buscando América")
    }
}
