package com.example.learningapp.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.R

@Composable
fun Avatar(visemeId: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(300.dp),
        contentAlignment = Alignment.Center) {

        // Base Avatar
        Image(
            painter = painterResource(id = R.drawable.avatar_base),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Mouth Avatar
        Image(
            painter = painterResource(id = getVisemeDrawable(visemeId)),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 50.dp)
                .size(70.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    Avatar(visemeId = 7)
}