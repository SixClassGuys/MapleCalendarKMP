package com.sixclassguys.maplecalendar.utils

import androidx.annotation.DrawableRes
import com.sixclassguys.maplecalendar.R

enum class RegionCategory(
    val code: String,
    val displayName: String,
    @DrawableRes val iconRes: Int
) {

    MAIN("MAIN", "메인", R.drawable.ic_map_main),

    /*** 메이플 월드 ***/
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

    // 메이플 아일랜드, 리엔, 에레브
    MAPLEISLAND("MAPLEISLAND", "메이플 아일랜드", R.drawable.ic_map_maple_island),
    RIEN("RIEN", "리엔", R.drawable.ic_map_rien),
    EREV("EREV", "에레브", R.drawable.ic_map_erev),

    // 엘나스 산맥
    ORBIS("ORBIS", "오르비스", R.drawable.ic_map_orbis),
    ELNATH("ELNATH", "엘나스", R.drawable.ic_map_el_nath),
    DEADMINE("DEADMINE", "폐광", R.drawable.ic_map_dead_mine),

    // 아쿠아로드
    AQUAROAD("AQUAROAD", "아쿠아로드", R.drawable.ic_map_aqua_road),

    // 루더스 호수
    LUDIBRIUM("LUDIBRIUM", "루디브리엄", R.drawable.ic_map_ludibrium),
    CLOCKTOWERBOTTOMFLOOR("CLOCKTOWERBOTTOMFLOOR", "시계탑 최하층", R.drawable.ic_map_clock_tower_bottom_floor),
    OMEGASECTOR("OMEGASECTOR", "지구방위본부", R.drawable.ic_map_omega_sector),
    FOLKTOWN("FOLKTOWN", "아랫마을", R.drawable.ic_map_folk_town),

    // 무릉도원
    MULUNGGARDEN("MULUNGGARDEN", "무릉", R.drawable.ic_map_mu_lung_garden),
    HERBTOWN("HERBTOWN", "백초마을", R.drawable.ic_map_herb_town),

    // 니할 사막
    ARIANT("ARIANT", "아리안트", R.drawable.ic_map_ariant),
    MAGATIA("MAGATIA", "마가티아", R.drawable.ic_map_magatia),
    ANCIENTCITYAZWAN("ANCIENTCITYAZWAN", "고대 도시 아스완", R.drawable.ic_map_ancient_city_azwan),

    // 미나르숲
    LEAFRE("LEAFRE", "리프레", R.drawable.ic_map_leafre),
    LEAFREDUNGEON("LEAFREDUNGEON", "리프레: 깊은 숲 던전", R.drawable.ic_map_leafre_dungeon),

    // 시간의 신전
    TEMPLEOFTIME("TEMPLEOFTIME", "시간의 신전", R.drawable.ic_map_temple_of_time),
    ARKARIUM("ARKARIUM", "아카이럼", R.drawable.ic_map_arkarium),
    GATEOFTHEFUTURE("GATEOFTHEFUTURE", "미래의 문", R.drawable.ic_map_gate_to_the_future),

    // 에델슈타인
    EDELSTEIN("EDELSTEIN", "에델슈타인", R.drawable.ic_map_edelstein),
    VERNEMINE("VERNEMINE", "레벤 광산", R.drawable.ic_map_verne_mine),
    SCRAPYARD("SCRAPYARD", "헤이븐", R.drawable.ic_map_scrapyard),

    // 크리티아스
    KRITIAS("KRITIAS", "크리티아스", R.drawable.ic_map_kritias),

    // 더 시드
    UNDERSEATOWEROFOZ("UNDERSEATOWEROFOZ", "더 시드", R.drawable.ic_map_undersea_tower_of_oz),

    /*** 그란디스 ***/
    // 판테온, 헬리시움
    PANTHEON("PANTHEON", "판테온", R.drawable.ic_map_pantheon),
    HELISEUM("HELISEUM", "헬리시움", R.drawable.ic_map_heliseum),
    TYRANTSCASTLE("TYRANTSCASTLE", "폭군의 성채", R.drawable.ic_map_tyrants_castle),

    // 미우미우
    VULPES("VULPES", "미우미우", R.drawable.ic_map_vulpes),

    // 베르딜
    VERDEL("VERDEL", "베르딜", R.drawable.ic_map_verdel),

    // 청운골
    CHEONGWOONVALLEY("CHEONGWOONVALLEY", "청운골", R.drawable.ic_map_cheong_woon_valley),

    // 리스토니아
    RISTONIA("RISTONIA", "리스토니아", R.drawable.ic_map_ristonia),

    // 나린
    NARIN("NARIN", "나린", R.drawable.ic_map_narin),

    // 에리모스
    ERIMOS("ERIMOS", "에리모스", R.drawable.ic_map_erimos),

    // 발로라
    VALLORA("VALLORA", "발로라", R.drawable.ic_map_vallora),

    // 새비지 터미널
    SAVAGETERMINAL("SAVAGETERMINAL", "새비지 터미널", R.drawable.ic_map_savage_terminal),

    // 아쉴롬
    SANCTUARY("SANCTUARY", "아쉴롬", R.drawable.ic_map_sanctuary),

    // 툴렌시티
    TOOLENCITY("TOOLENCITY", "툴렌시티", R.drawable.ic_map_toolen_city),

    // 검은 바다
    DARKSEA("DARKSEA", "검은 바다", R.drawable.ic_map_dark_sea),

    // 세르니움
    CERNIUM("CERNIUM", "세르니움", R.drawable.ic_map_cernium),

    // 호텔 아르크스
    HOTELARCUS("HOTELARCUS", "호텔 아르크스", R.drawable.ic_map_hotel_arcus),

    // 카로테
    KAROTE("KAROTE", "카로테", R.drawable.ic_map_karote),

    // 오디움
    ODIUM("ODIUM", "오디움", R.drawable.ic_map_odium),

    // 도원경
    SHANGRILA("SHANGRILA", "도원경", R.drawable.ic_map_shangri_la),

    // 아르테리아
    ARTERIA("ARTERIA", "아르테리아", R.drawable.ic_map_arteria),

    // 카르시온
    CARCION("CARCION", "카르시온", R.drawable.ic_map_carcion),

    // 탈라하트
    TALLAHART("TALLAHART", "탈라하트", R.drawable.ic_map_tallahart),

    // 기어드락
    GEARDRAK("GEARDRAK", "기어드락", R.drawable.ic_map_geardrak),

    /*** 거울세계 ***/
    // 거울세계
    MIRRORWORLD("MIRRORWORLD", "거울세계", R.drawable.ic_map_mirror_world),

    /*** 아케인리버 ***/
    // 테네브리스 이전
    VANISHINGJOURNEY("VANISHINGJOURNEY", "소멸의 여로", R.drawable.ic_map_vanishing_journey),
    REVERSECITY("REVERSECITY", "리버스 시티", R.drawable.ic_map_reverse_city),
    CHUCHUISLAND("CHUCHUISLAND", "츄츄 아일랜드", R.drawable.ic_map_chu_chu_island),
    YUMYUMISLAND("YUMYUMISLAND", "얌얌 아일랜드", R.drawable.ic_map_yum_yum_island),
    LACHELEIN("LACHELEIN", "꿈의 도시 레헬른", R.drawable.ic_map_lachelein),
    ARCANA("ARCANA", "신비의 숲 아르카나", R.drawable.ic_map_arcana),
    MORASS("MORASS", "기억의 늪 모라스", R.drawable.ic_map_morass),
    ESFERA("ESFERA", "태초의 바다 에스페라", R.drawable.ic_map_esfera),
    SELLAS("SELLAS", "셀라스, 별이 잠긴 곳", R.drawable.ic_map_sellas),

    // 테네브리스
    MOONBRIDGE("MOONBRIDGE", "문브릿지", R.drawable.ic_map_moonbridge),
    LABYRINTHOFSUFFERING("LABYRINTHOFSUFFERING", "고통의 미궁", R.drawable.ic_map_labyrinth_of_suffering),
    LIMINA("LIMINA", "리멘", R.drawable.ic_map_limina);

    /*** 파티 퀘스트 ***/

    /*** 테마던전 ***/

    /*** 차원의 도서관 ***/

    /*** 프렌즈스토리 ***/

    /*** 블록버스터 ***/

    /*** 기타 시스템, 콘텐츠 ***/

    /*** 이벤트 ***/

    /*** 해외지역 ***/

    companion object {

        fun fromCode(code: String?): RegionCategory {
            return entries.find { it.code == code?.uppercase() } ?: MAIN
        }
    }
}