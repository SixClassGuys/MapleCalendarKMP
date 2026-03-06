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
    KERNINGCITY("KERNINGCITY", "커닝시티", R.drawable.ic_map_kerning_city),
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
    LIMINA("LIMINA", "리멘", R.drawable.ic_map_limina),

    /*** 파티 퀘스트 ***/
    LUDIBRIUMPQ("LUDIBRIUMPQ", "차원의 균열", R.drawable.ic_map_ludibrium_pq),
    ORBISPQ("ORBISPQ", "여신의 흔적", R.drawable.ic_map_orbis_pq),
    KREASE("KREASE", "크리세의 크세르크세스", R.drawable.ic_map_krease),
    PARTYQUEST("PARTYQUEST", "파티 퀘스트", R.drawable.ic_map_party_quest),

    /*** 테마던전 ***/
    GOLDBEACH("GOLDBEACH", "골드비치", R.drawable.ic_map_gold_beach),
    RIENASTRAIT("RIENASTRAIT", "리에나 해협", R.drawable.ic_map_riena_strait),
    ELLINELFAIRYACADEMY("ELLINELFAIRYACADEMY", "요정학원 엘리넬", R.drawable.ic_map_ellinel_fairy_academy),
    ELODIN("ELODIN", "비밀의 숲 엘로딘", R.drawable.ic_map_elodin),
    MUSHROOMCASTLE("MUSHROOMCASTLE", "버섯의 성", R.drawable.ic_map_mushroom_castle),
    ELLINFOREST("ELLINFOREST", "엘린숲", R.drawable.ic_map_ellin_forest),
    VONLEON("VONLEON", "사자왕의 성", R.drawable.ic_map_von_leon),
    FANTASTICTHEMEPARK("FANTASTICTHEMEPARK", "판타스틱 테마파크", R.drawable.ic_map_fantastic_theme_park),
    KERNINGTOWER("KERNINGTOWER", "커닝 타워", R.drawable.ic_map_kerning_tower),
    STONECOLOSSUS("STONECOLOSSUS", "암벽거인 콜로서스", R.drawable.ic_map_stone_colossus),
    FOXVALLEY("FOXVALLEY", "여우골짜기", R.drawable.ic_map_fox_valley_theme_dungeon),

    /*** 차원의 도서관 ***/
    GRANDATHENAEUM("GRANDATHENAEUM", "차원의 도서관", R.drawable.ic_map_grand_athenaeum),

    /*** 프렌즈스토리 ***/
    FRIENDSSTORY("FRIENDSSTORY", "프렌즈스토리", R.drawable.ic_map_friends_story),

    /*** 블록버스터 ***/
    BLACKHEAVEN("BLACKHEAVEN", "블랙헤븐", R.drawable.ic_map_black_heaven),
    HEROESOFMAPLE("HEROESOFMAPLE", "히어로즈 오브 메이플", R.drawable.ic_map_heroes_of_maple),

    /*** 에픽던전 ***/
    HIGHMOUNTAIN("HIGHMOUNTAIN", "하이마운틴", R.drawable.ic_map_high_mountain),
    THEANGLERCOMPANY("THEANGLERCOMPANY", "앵글러 컴퍼니", R.drawable.ic_map_the_angler_company),
    NIGHTMAREPARADISE("NIGHTMAREPARADISE", "악몽선경", R.drawable.ic_map_nightmare_paradise),

    /*** 보스 카이 ***/
    BOSSKAI("BOSSKAI", "넥서스 타워 : 보스 카이", R.drawable.ic_map_boss_kai),

    /*** 기타 시스템, 콘텐츠 ***/
    CASHSHOP("CASHSHOP", "캐시샵", R.drawable.ic_map_cash_shop),
    MEISTERVILLE("MEISTERVILLE", "마이스터빌", R.drawable.ic_map_meister_ville),
    GUILD("GUILD", "길드 콘텐츠", R.drawable.ic_map_guild),
    ELITEBOSS("ELITEBOSS", "엘리트 보스", R.drawable.ic_map_elite_boss),
    MONSTERPARK("MONSTERPARK", "몬스터 파크", R.drawable.ic_map_monster_park),
    MULUNGDOJO("MULUNGDOJO", "무릉도장", R.drawable.ic_map_mu_lung_dojo),
    SIXTHJOB("SIXTHJOB", "6차 전직", R.drawable.ic_map_6th_job),
    UNIONCHAMPION("UNIONCHAMPION", "유니온 챔피언", R.drawable.ic_map_union_champion),
    WORLDBOSS("WORLDBOSS", "차원의 전장", R.drawable.ic_map_world_boss),
    MVPRESORT("MVPRESORT", "MVP 리조트", R.drawable.ic_map_mvp_resort),
    GUILDCASTLE("GUILDCASTLE", "길드 캐슬", R.drawable.ic_map_guild_castle),

    /*** 종료된 콘텐츠 ***/
    SUPERFIGHT("SUPERFIGHT", "대난투", R.drawable.ic_map_super_fight),
    BATTLEMONSTERLEAGUE("BATTLEMONSTERLEAGUE", "배몬 리그", R.drawable.ic_map_battle_monster_league),
    STARPLANET("STARPLANET", "스타플래닛", R.drawable.ic_map_star_planet),
    GODOFCONTROL("GODOFCONTROL", "갓 오브 컨트롤", R.drawable.ic_map_god_of_control),
    EVOLUTIONSYSTEM("EVOLUTIONSYSTEM", "이볼빙 시스템", R.drawable.ic_map_evolution_system),
    MONSTERLIFE("MONSTERLIFE", "몬스터 라이프", R.drawable.ic_map_monster_life),
    URSUS("URSUS", "파왕 우르스", R.drawable.ic_map_ursus),
    BOUNTYHUNTER("BOUNTYHUNTER", "현상금 사냥꾼", R.drawable.ic_map_bounty_hunter),
    AZMOTHCANYON("AZMOTHCANYON", "아즈모스 협곡", R.drawable.ic_map_azmoth_canyon),

    /*** 아이템 ***/
    ITEMS("ITEMS", "아이템", R.drawable.ic_map_items),

    /*** 이벤트 ***/
    HALLOWEEN("HALLOWEEN", "할로윈 이벤트", R.drawable.ic_map_halloween),
    EVENTADVENTURE("EVENTADVENTURE", "어드벤처 아일랜드", R.drawable.ic_map_event_adventure),
    EVENTRISE("EVENTRISE", "살롱 RISE", R.drawable.ic_map_event_rise),
    EVENTAWAKE("EVENTAWAKE", "각성의 산", R.drawable.ic_map_event_awake),
    EVENTMAPLELIVE("EVENTMAPLELIVE", "메이플 LIVE", R.drawable.ic_map_event_maple_live),
    EVENTIGNITION("EVENTIGNITION", "이그니션 불꽃 축제", R.drawable.ic_map_event_ignition),
    EVENTNEWAGE("EVENTNEWAGE", "이덴티스크", R.drawable.ic_map_event_new_age),
    EVENTMILESTONE("EVENTMILESTONE", "기억 속의 한 페이지", R.drawable.ic_map_event_milestone),
    EVENTNEXT("EVENTNEXT", "카르시온 옥토 페스타", R.drawable.ic_map_event_next),

    /*** 주년 이벤트 ***/
    EVENT11TH("EVENT11TH", "11주년 : 태초에 버섯이 있었다", R.drawable.ic_map_11th_event),
    EVENT12TH("EVENT12TH", "12주년 : 몬스터 보물창고", R.drawable.ic_map_12th_event),
    EVENT13TH("EVENT13TH", "13주년 : 13주년의 금요일", R.drawable.ic_map_13th_event),
    EVENT14TH("EVENT14TH", "14주년 : 우주 Like 메이플?", R.drawable.ic_map_14th_event),
    EVENT15TH("EVENT15TH", "15주년 : 메이플 15번가", R.drawable.ic_map_15th_event),
    EVENT16TH("EVENT16TH", "16주년 : 뉴트로 타임", R.drawable.ic_map_16th_event),
    EVENT17TH("EVENT17TH", "17주년 : 호텔 메이플", R.drawable.ic_map_17th_event),
    EVENT18TH("EVENT18TH", "18주년 : 블루밍 포레스트", R.drawable.ic_map_18th_event),
    EVENT19TH("EVENT19TH", "19주년 : 메이플 모멘트리", R.drawable.ic_map_19th_event),
    EVENT20TH("EVENT20TH", "20주년 : 메이프릴 아일랜드", R.drawable.ic_map_20th_event),
    EVENT21TH("EVENT21TH", "21주년 : 빅토리아컵", R.drawable.ic_map_21th_event),
    EVENT22TH("EVENT22TH", "22주년 : 메이플 대학교", R.drawable.ic_map_22th_event),

    /*** 해외지역 ***/
    // GMS
    NEWLEAFCITY("NEWLEAFCITY", "뉴리프 시티", R.drawable.ic_map_new_leaf_city),
    MASTERIA("MASTERIA", "마스테리아", R.drawable.ic_map_masteria),
    NLCUNDERATTACK("NLCUNDERATTACK", "공격 받은 뉴리프 시티", R.drawable.ic_map_nlc_under_attack),
    AMORIA("AMORIA", "아모리아", R.drawable.ic_map_amoria),
    TYNERUM("TYNERUM", "타이네럼", R.drawable.ic_map_tynerum),
    ARBOREN("ARBOREN", "아브렌", R.drawable.ic_map_arboren),
    COMMERCI("COMMERCI", "코메르츠 공화국", R.drawable.ic_map_commerci),
    MRLEEWORLDTOURDUNGEON("MRLEEWORLDTOURDUNGEON", "2014 미스터 리의 세계 여행 이벤트", R.drawable.ic_map_mr_lee_world_tour_dungeon),
    MONAD("MONAD", "모나드: 첫 번째 징조", R.drawable.ic_map_monad),
    MASTERIAREMASTERED("MASTERIAREMASTERED", "마스테리아(개편 이후)", R.drawable.ic_map_masteria_remastered),
    AFTERLAND("AFTERLAND", "애프터랜드", R.drawable.ic_map_afterland),
    STELLADETECTIVES("STELLADETECTIVES", "스텔라 탐정단", R.drawable.ic_map_stella_detectives),
    MYHOME("MYHOME", "마이홈", R.drawable.ic_map_my_home),

    // 일본(JMS)
    MUSHROOMSHRINE("MUSHROOMSHRINE", "버섯신사", R.drawable.ic_map_mushroom_shrine),
    SHOWATOWN("SHOWATOWN", "쇼와 마을", R.drawable.ic_map_showa_town),
    OUTSIDENINJACASTLE("OUTSIDENINJACASTLE", "카에데 성", R.drawable.ic_map_outside_ninja_castle),
    NEOTOKYO("NEOTOKYO", "네오 도쿄", R.drawable.ic_map_neo_tokyo),
    TOKYO("TOKYO", "도쿄", R.drawable.ic_map_tokyo),
    MOMIJIGAOKA("MOMIJIGAOKA", "모미지 언덕", R.drawable.ic_map_momijigaoka),
    HIEIJAN("HIEIJAN", "히에이잔", R.drawable.ic_map_hieijan),
    SILENTCRUSADE("SILENTCRUSADE", "항마의 십자여단", R.drawable.ic_map_silent_crusade),
    ATTACKONTITAN("ATTACKONTITAN", "메이플스토리 X 진격의 거인", R.drawable.ic_map_attack_on_titan),
    PACHINKO("PACHINKO", "일본 파칭코", R.drawable.ic_map_pachinko),
    PACHINKOREACH("PACHINKOREACH", "일본 파칭코 REACH", R.drawable.ic_map_pachinko_reach),
    ASURAWAR("ASURAWAR", "전국시대 3: 수라의 난", R.drawable.ic_map_asura_war),

    // 중국(CMS)
    SHANGHAI("SHANGHAI", "상해와이탄", R.drawable.ic_map_shanghai),
    SHAOLIN("SHAOLIN", "소림사", R.drawable.ic_map_shaolin),
    YUYUAN("YUYUAN", "동방신주 예원", R.drawable.ic_map_yuyuan),
    DRAGONTIGERFIELD("DRAGONTIGERFIELD", "와호장룡 도장", R.drawable.ic_map_dragon_tiger_field),
    CMSEVENT("CMSEVENT", "CMS 이벤트", R.drawable.ic_map_cms_event),

    // 대만(TMS)
    XIMENDING("XIMENDING", "서문정", R.drawable.ic_map_ximending),
    TAIPEI101("TAIPEI101", "타이베이 101", R.drawable.ic_map_taipei_101),
    ARISAN("ARISAN", "아리산", R.drawable.ic_map_arisan),
    CHIVALROUSFIGHTER("CHIVALROUSFIGHTER", "용의 전인", R.drawable.ic_map_chivalrous_fighter),
    TMSEVENT("TMSEVENT", "TMS 이벤트", R.drawable.ic_map_tms_event),
    ETERNALFOREST("ETERNALFOREST", "영원의 숲", R.drawable.ic_map_eternal_forest),
    XUANSHAN("XUANSHAN", "현산", R.drawable.ic_map_xuanshan),

    // 동남아시아(MSEA)
    FLOATINGMARKET("FLOATINGMARKET", "플로팅 마켓", R.drawable.ic_map_floating_market),
    GOLDENTEMPLE("GOLDENTEMPLE", "황금사원", R.drawable.ic_map_golden_temple),
    SINGAPORE("SINGAPORE", "싱가포르", R.drawable.ic_map_singapore),
    MALAYSIA("MALAYSIA", "말레이시아", R.drawable.ic_map_malaysia),
    MSEAEVENT("MSEAEVENT", "MSEA 이벤트", R.drawable.ic_map_msea_event),

    // 브라질
    BRAZILEVENT("BRAZILEVENT", "브라질 서버 이벤트", R.drawable.ic_map_brazil_event);

    // 이벤트

    // 아이템

    companion object {

        fun fromCode(code: String?): RegionCategory {
            return entries.find { it.code == code?.uppercase() } ?: MAIN
        }
    }
}