package com.example.kingsofkodeimport android.content.Contextimport android.content.DialogInterfaceimport android.graphics.drawable.Drawableimport android.media.MediaPlayerimport android.os.Bundleimport android.view.View.GONEimport android.widget.*import androidx.appcompat.app.AlertDialogimport androidx.appcompat.app.AppCompatActivityimport androidx.core.content.ContextCompatimport com.example.kingsofkode.core.Gameimport kotlinx.android.synthetic.main.board_layout.*class BoardGameActivity : AppCompatActivity() {    private lateinit var game: Game    private var listOfDiceState = BooleanArray(6){false}    private val dice = ArrayList<ImageButton>()    private val characterCardList = ArrayList<ImageView>()    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.board_layout)        val playerName = this.intent.getStringExtra("player")!!        this.game = Game(playerName)        dice.addAll(arrayOf(                findViewById(R.id.die1),                findViewById(R.id.die2),                findViewById(R.id.die3),                findViewById(R.id.die4),                findViewById(R.id.die5),                findViewById(R.id.die6)            )        )        characterCardList.addAll(arrayOf(            findViewById(R.id.center_character),            findViewById(R.id.character_one),            findViewById(R.id.character_two),            findViewById(R.id.character_three)        ))        for (i in 0 until this.dice.size) {            val die = this.dice[i]            die.setOnClickListener { onDiceClick(i, die) }        }        setGameState()        val rollButton = findViewById<Button>(R.id.roll_button)        rollButton.setOnClickListener{            val mediaPlayer: MediaPlayer? = MediaPlayer.create(this, R.raw.dice_roll)            mediaPlayer?.start()            if (rollButton.text == "SUBMIT") {                // play first NPC after player's turn                game.turn()                rollButton.text = "NEXT TURN"                setGameState()            }            else if (rollButton.text == "NEXT TURN") {                if (game.player == game.king && game.playerWasHit) {                    val onPositive: (dialog: DialogInterface, which: Int) -> Unit = { dialog, which ->                        game.playerAbdicates()                        setGameState()                        Toast.makeText(this, "You abdicated", Toast.LENGTH_SHORT).show()                    }                    val onNegative: (dialog: DialogInterface, which: Int) -> Unit = { dialog, which ->                        Toast.makeText(this, "You keep the king's place !", Toast.LENGTH_SHORT).show()                    }                    this.showDialogAlert("Abdicate", "Do you want to abdicate ?", onPositive, onNegative)                }                resetDiceUI()                Thread.sleep(500) // TODO: Figure out why sleeping doesn't work as expected                // end NPC turn                game.turn()                setGameState()                // if it's player's turn now                if (game.player == game.currentPlayer) {                    rollButton.text = "ROLL"                    resetDiceUI()                }            }            else {                // do a re-roll                val diceStates = ArrayList<Int>()                for (i in listOfDiceState.indices) {                    if (listOfDiceState[i]) {                        diceStates.add(i)                    }                }                if (game.rollsRemaining == 3) {                    diceStates.addAll(listOf(0, 1, 2, 3, 4, 5))                }                game.reRoll(diceStates)                diceUpdate()                // reset dice                listOfDiceState = BooleanArray(6){false}                for (i in 0 until this.dice.size) {                    val die = this.dice[i]                    die.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBackground))                }                roll_button.text = "SUBMIT"            }        }    }    private fun resetDiceUI() {        for (i in 0 until this.dice.size) {            val die = this.dice[i]            die.setImageDrawable(getDrawableFromString(this, "die"))        }    }    private fun diceUpdate() {        // updates dice in the UI with dice in the game state        for (i in 0 until this.dice.size) {            val die = this.dice[i]            die.setImageDrawable(getDrawableFromString(this, game.dice[i]))        }    }    private fun onDiceClick(dieNumber: Int, die: ImageButton) {        // what happens when a dice icon is clicked        if (game.rollsRemaining == 0            || roll_button.text == "ROLL"            || game.currentPlayer != game.player        ) {            return        }        listOfDiceState[dieNumber] = !listOfDiceState[dieNumber]        if (listOfDiceState[dieNumber])            die.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))        else            die.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBackground))        val rollButton = findViewById<Button>(R.id.roll_button)        if (listOfDiceState.find{ it } == null) {            rollButton.text = "SUBMIT"        }        else {            rollButton.text = "RE-ROLL"        }    }    private fun setGameState() {        if (game.state != "running") {            if (game.state == "win") {                // TODO: move to victory state            }            else {                // TODO: move to defeat state            }        }        // set the dice        diceUpdate()        statusUpdate()        characterCardUpdate()    }    private fun statusUpdate() {        val score = findViewById<TextView>(R.id.popularity_points)        val energy = findViewById<TextView>(R.id.research_points)        val health = findViewById<TextView>(R.id.breaking_change_points)        score.text = game.player.score.toString()        energy.text = game.player.energy.toString()        health.text = game.player.health.toString()        score.invalidate()        energy.invalidate()        health.invalidate()    }    private fun characterCardUpdate() {        characterCardList[0].setImageDrawable(getDrawableFromString(this, game.king.name))        val simpleCharacterCardList = characterCardList.subList(1, characterCardList.size)        val simpleCharacterList = game.characters.filter { it != game.king }        for (i in 0 until simpleCharacterCardList.size) {            val character = simpleCharacterList[i]            val simpleCharacterCard = simpleCharacterCardList[i]            simpleCharacterCard.setImageDrawable(getDrawableFromString(this, character.name))            if (!character.isAlive()) {                simpleCharacterCard.visibility = GONE                // TODO Hide simple character's card here            }        }    }    private fun getDrawableFromString(context: Context, drawableName: String) : Drawable? {        return context.let { ContextCompat.getDrawable(            it, resources.getIdentifier(                drawableName,                "drawable", context.packageName            )        )}    }    private fun showDialogAlert(        title: String,        message: String,        onPositive: (dialog: DialogInterface, which: Int) -> Unit,        onNegative: (dialog: DialogInterface, which: Int) -> Unit) {        val builder = AlertDialog.Builder(this)        builder.setTitle(title)        builder.setMessage(message)        builder.setPositiveButton("Yes") { dialog, which -> onPositive(dialog, which) }        builder.setNegativeButton("No") { dialog, which -> onNegative(dialog, which)}        builder.show()    }}