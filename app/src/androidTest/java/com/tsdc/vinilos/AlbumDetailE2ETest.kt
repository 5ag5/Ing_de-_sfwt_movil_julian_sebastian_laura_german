package com.tsdc.vinilos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsdc.vinilos.ui.MainActivity
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * E2E UI (HU02): tab Álbumes → tocar el primer álbum en lista → ver pantalla de detalle.
 */
@RunWith(AndroidJUnit4::class)
class AlbumDetailE2ETest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun openFirstAlbumDetail() {
        composeRule.onNodeWithText("ALBUMS").assertExists()
        composeRule.onNodeWithText("ALBUMS").performClick()

        composeRule.waitUntil(timeoutMillis = 20_000) {
            composeRule
                .onAllNodesWithTag(UiTestTags.ALBUM_LIST_ITEM, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule
            .onAllNodesWithTag(UiTestTags.ALBUM_LIST_ITEM, useUnmergedTree = true)
            .onFirst()
            .performClick()

        composeRule.onNodeWithTag(UiTestTags.ALBUM_DETAIL_ROOT, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun fromAlbumsTab_tapFirstItem_showsAlbumDetail() {
        openFirstAlbumDetail()

        composeRule.onNodeWithText("Detalle del álbum").assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.ALBUM_DETAIL_ROOT, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun fromAlbumsTab_tapFirstItem_showsAlbumMetadata() {
        openFirstAlbumDetail()

        composeRule.onNodeWithText("Fecha de lanzamiento").assertIsDisplayed()
        composeRule.onNodeWithText("Género").assertIsDisplayed()
        composeRule.onNodeWithText("Sello discográfico").assertIsDisplayed()
    }

    @Test
    fun fromAlbumsTab_tapFirstItem_showsDescriptionAndBackButton() {
        openFirstAlbumDetail()

        composeRule.onNodeWithText("Descripción").assertIsDisplayed()
        composeRule.onNodeWithText("Volver").assertIsDisplayed()
    }

    @Test
    fun fromAlbumDetail_tapBack_returnsToAlbumsTab() {
        openFirstAlbumDetail()

        composeRule.onNodeWithText("Volver").performClick()

        composeRule.onNodeWithText("YOUR LIBRARY").assertIsDisplayed()
        composeRule.waitUntil(timeoutMillis = 20_000) {
            composeRule
                .onAllNodesWithTag(UiTestTags.ALBUM_LIST_ITEM, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}
