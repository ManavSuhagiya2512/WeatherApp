/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.database.model

import androidx.room.Entity
import androidx.room.Fts4

@Entity
@Fts4(contentEntity = Favorite::class)
data class FavoriteFts(
    val id: Int,
    val name: String
)
