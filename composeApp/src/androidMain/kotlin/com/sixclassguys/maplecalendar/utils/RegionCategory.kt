package com.sixclassguys.maplecalendar.utils

import androidx.annotation.DrawableRes
import com.sixclassguys.maplecalendar.R

enum class RegionCategory(
    val code: String,
    val displayName: String,
    @DrawableRes val iconRes: Int
) {

    MAIN("MAIN", "메인", R.drawable.ic_map_main),

    // 빅토리아 아일랜드
    LITHHARBOR("LITHHARBOR", "리스항구", R.drawable.ic_map_lith_harbor),
    HENESYS("HENESYS", "헤네시스", R.drawable.ic_map_henesys),
    ELLINIA("ELLINIA", "엘리니아", R.drawable.ic_map_ellinia),
    PERION("PERION", "페리온", R.drawable.ic_map_perion),
    KERNINGCITY("KERNING", "커닝시티", R.drawable.ic_map_kerning_city),
    SLEEPYWOOD("SLEEPYWOOD", "슬리피우드", R.drawable.ic_map_sleepywood),
    NAUTILUS("NAUTILUS", "노틸러스", R.drawable.ic_map_nautilus),
    ELLUEL("ELLUEL", "에우렐", R.drawable.ic_map_elluel),
    PARTEM("PARTEM", "파르템", R.drawable.ic_map_partem),
    ROOTABYSS("ROOTABYSS", "루타비스", R.drawable.ic_map_root_abyss),


    ORBIS("ORBIS", "오르비스", R.drawable.ic_map_orbis),
    LUDIBRIUM("LUDIBRIUM", "루디브리엄", R.drawable.ic_map_ludibrium),
    EDELSTEIN("EDELSTEIN", "에델슈타인", R.drawable.ic_map_edelstein),
    RISTONIA("RISTONIA", "리스토니아", R.drawable.ic_map_ristonia);

    companion object {

        fun fromCode(code: String?): RegionCategory {
            return entries.find { it.code == code?.uppercase() } ?: MAIN
        }
    }
}