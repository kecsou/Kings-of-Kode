package com.example.kingsofkode.core

class DataSource {
    companion object {
        fun getCards(game: Game):ArrayList<Card> {
            val player = game.player
            val npcPlayers = game.characters.filter { it != player }
            val cardsShuffled = arrayListOf<Card>()

            val cards = mutableListOf(
                Card("card_damage_2", 7) {
                    // Example this card hit all npc players of one
                    for (npcPlayer in npcPlayers) {
                        if (npcPlayer.isAlive()) {
                            npcPlayer.decreaseHealth(2)
                        }
                    }
                },
                Card("card_damage_3_all", 7) {
                    // Example this card hit all npc players of one
                    for (character in game.characters) {
                        if (character.isAlive()) {
                            player.decreaseHealth(3)
                        }
                    }
                },
                Card("card_score_vampire_2", 7) {
                    // Example this card hit all npc players of one
                    var highestScoringPlayer = game.player
                    var highestScore = 0
                    for ( character in  game.characters) {
                        if (!character.isAlive()) {
                            continue
                        }
                        if (character.score > highestScore) {
                            highestScore = character.score
                            highestScoringPlayer = character
                        }
                    }
                    player.score += 2
                    highestScoringPlayer.score -= 2
                },
                Card("card_damage_3_king", 8) {
                    // Example this card hit all npc players of one
                    game.king.decreaseHealth(3)
                },
                Card("card_score_3", 6) {
                    // Example this card hit all npc players of one
                    player.score += 3
                },
                Card("card_health_3", 3) {
                    // Increase player heath by 3
                    player.increaseHealth(3)
                },
                Card("card_health_5", 7) {
                    // Increase player heath by 5
                    player.increaseHealth(5)
                },
                Card("card_health_55", 8) {
                    // Increase player heath (without 10 max check)
                    player.health += 5
                }
            )
            cards.shuffle()

            for (card in cards) {
                cardsShuffled.add(card)
            }

            return cardsShuffled
        }
    }
}