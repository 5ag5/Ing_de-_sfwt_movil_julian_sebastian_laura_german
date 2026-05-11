package com.tsdc.vinilos

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsdc.vinilos.domain.usecases.GetAlbumsUseCase
import com.tsdc.vinilos.domain.usecases.GetArtistsUseCase
import com.tsdc.vinilos.domain.usecases.GetCollectorsUseCase
import com.tsdc.vinilos.ui.screens.HomeScreen
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.AlbumViewModel
import com.tsdc.vinilos.ui.viewmodels.ArtistViewModel
import com.tsdc.vinilos.ui.viewmodels.CollectorViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorListComposeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun launchHomeWithFakes() {
        composeRule.setContent {
            VinilosTheme {
                val albumViewModel = AlbumViewModel(GetAlbumsUseCase(FakeAlbumRepository()))
                val artistViewModel = ArtistViewModel(GetArtistsUseCase(FakeArtistRepository()))
                val collectorViewModel =
                    CollectorViewModel(GetCollectorsUseCase(FakeCollectorRepository()))
                HomeScreen(
                    albumViewModel = albumViewModel,
                    artistViewModel = artistViewModel,
                    collectorViewModel = collectorViewModel,
                    initialTab = 0,
                    onAlbumClick = {}
                )
            }
        }
    }

    private fun navigateToCollectors() {
        composeRule.onNodeWithTag("nav_collectors").performClick()
    }

    private fun waitForCollectorItems(timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule
                .onAllNodesWithTag(UiTestTags.COLLECTOR_LIST_ITEM, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun collectorsTab_showsSectionsAndCatalogNames() {
        launchHomeWithFakes()
        navigateToCollectors()

        composeRule.onNodeWithText("Collectors").assertIsDisplayed()
        composeRule.onNodeWithText("COMMUNITY").assertIsDisplayed()
        composeRule.onNodeWithText("CURATORS & ENTHUSIASTS").assertIsDisplayed()

        waitForCollectorItems()
        composeRule.onNodeWithText("Manolo Bellon").assertIsDisplayed()
        composeRule.onNodeWithText("Jaime Monsalve").assertIsDisplayed()
    }

    @Test
    fun collectorsSearch_filtersByEmail() {
        launchHomeWithFakes()
        navigateToCollectors()
        waitForCollectorItems()

        composeRule.onNodeWithTag(UiTestTags.COLLECTOR_SEARCH_FIELD).performTextReplacement("jmonsalve")
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Jaime Monsalve").assertIsDisplayed()
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText("Manolo Bellon").fetchSemanticsNodes().isEmpty()
        }
    }
}
