package com.example.data.model

data class MarketOutcome(
    val id: String,
    val label: String,        // e.g. "1", "X", "2", "Over 2.5", "Under 2.5", "Yes", "No"
    val odds: Double,
    val previousOdds: Double = odds,
    val oddsDirection: OddsDirection = OddsDirection.NONE
)

enum class OddsDirection {
    NONE, UP, DOWN
}

data class Market(
    val id: String,
    val name: String,         // e.g. "1X2 (Match Winner)", "Over/Under 2.5", "Both Teams to Score", "Double Chance"
    val category: String = "Main", // Main, Goals, Halftime, Corners, Specials
    val outcomes: List<MarketOutcome>
)

data class Match(
    val id: String,
    val sport: String,        // "Football", "Basketball", "Tennis", "Cricket", "eSports"
    val league: String,       // e.g. "UEFA Champions League", "Premier League"
    val country: String,      // "Europe", "England", "Spain", "USA"
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int? = null,
    val awayScore: Int? = null,
    val matchStatus: MatchStatus = MatchStatus.UPCOMING,
    val matchTime: String,    // "20:00" or "Live 74'"
    val liveMinute: Int? = null,
    val isLive: Boolean = false,
    val markets: List<Market>,
    // Live match field telemetry for pitch visualizer
    val possessionHome: Int = 50,
    val possessionAway: Int = 50,
    val attacksHome: Int = 24,
    val attacksAway: Int = 19,
    val dangerousAttacksHome: Int = 12,
    val dangerousAttacksAway: Int = 9,
    val shotsOnTargetHome: Int = 4,
    val shotsOnTargetAway: Int = 2,
    val cornersHome: Int = 3,
    val cornersAway: Int = 2,
    val yellowCardsHome: Int = 1,
    val yellowCardsAway: Int = 2,
    val currentPitchAction: String = "Normal Play - Midfield Duel",
    val ballPositionPercentX: Float = 0.5f, // 0.0 (Home goal) to 1.0 (Away goal)
    val ballPositionPercentY: Float = 0.5f,
    val isFavorite: Boolean = false
)

enum class MatchStatus {
    LIVE, UPCOMING, FINISHED
}

data class BetSelection(
    val matchId: String,
    val homeTeam: String,
    val awayTeam: String,
    val league: String,
    val marketId: String,
    val marketName: String,
    val outcomeId: String,
    val outcomeLabel: String,
    val odds: Double,
    val liveScoreWhenPlaced: String = "",
    val selectionStatus: String = "PENDING" // PENDING, WON, LOST
)

enum class BetType {
    SINGLE, MULTIPLE, SYSTEM
}
