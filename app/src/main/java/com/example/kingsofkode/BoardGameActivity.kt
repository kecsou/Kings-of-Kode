package com.example.kingsofkodeimport android.content.Contextimport android.graphics.Colorimport android.graphics.drawable.Drawableimport android.media.MediaPlayerimport android.os.Bundleimport android.widget.Buttonimport android.widget.ImageButtonimport androidx.appcompat.app.AppCompatActivityimport androidx.core.content.ContextCompatimport com.example.kingsofkode.core.Gameclass BoardGameActivity : AppCompatActivity() {    private val playerName = this.intent.getStringExtra("player")    private var game : Game    private val context: Context? = null    private var listOfDiceState = BooleanArray(6){false}    init{        this.game = playerName?.let { Game(it) }!!    }    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.board_layout)        // Die state management        val die1 = findViewById<ImageButton>(R.id.die1)        val die2 = findViewById<ImageButton>(R.id.die2)        val die3 = findViewById<ImageButton>(R.id.die3)        val die4 = findViewById<ImageButton>(R.id.die4)        val die5 = findViewById<ImageButton>(R.id.die5)        val die6 = findViewById<ImageButton>(R.id.die6)        die1.setOnClickListener{onDiceClick(0, die1)}        die2.setOnClickListener{onDiceClick(1, die2)}        die3.setOnClickListener{onDiceClick(2, die3)}        die4.setOnClickListener{onDiceClick(3, die4)}        die5.setOnClickListener{onDiceClick(4, die5)}        die6.setOnClickListener{onDiceClick(5, die6)}        val rollButton = findViewById<Button>(R.id.roll_button)        rollButton.setOnClickListener{            val mediaPlayer: MediaPlayer? = MediaPlayer.create(this, R.raw.dice_roll)            mediaPlayer?.start()            if (rollButton.text == "SUBMIT") {                game.turn()                for (i in 1..game.charactersAlive) {                    game.turn()                    // TODO: manage if player is king and was hit                    if (game.player == game.king && game.playerWasHit) {                        // TODO: throw a notification window to player to ask if he wants to abdicate                        game.playerAbdicates()                    }                    setGameState()                    Thread.sleep(3_000)                }            }            else {                // do a re-roll                val diceStates = ArrayList<Int>()                for (i in 0..listOfDiceState.size) {                    if (listOfDiceState[i]) {                        diceStates.add(i)                    }                }                game.reRoll(diceStates)                diceUpdate()                // reset dice                listOfDiceState = BooleanArray(6){false}                die1.setBackgroundColor(Color.LTGRAY)                die2.setBackgroundColor(Color.LTGRAY)                die3.setBackgroundColor(Color.LTGRAY)                die4.setBackgroundColor(Color.LTGRAY)                die5.setBackgroundColor(Color.LTGRAY)                die6.setBackgroundColor(Color.LTGRAY)            }        }    }    private fun diceUpdate() {        // updates dice in the UI with dice in the game state        val die1 = findViewById<ImageButton>(R.id.die1)        val die2 = findViewById<ImageButton>(R.id.die2)        val die3 = findViewById<ImageButton>(R.id.die3)        val die4 = findViewById<ImageButton>(R.id.die4)        val die5 = findViewById<ImageButton>(R.id.die5)        val die6 = findViewById<ImageButton>(R.id.die6)        die1.setImageDrawable(getDrawableFromString(this, game.dice[0]))        die2.setImageDrawable(getDrawableFromString(this, game.dice[1]))        die3.setImageDrawable(getDrawableFromString(this, game.dice[2]))        die4.setImageDrawable(getDrawableFromString(this, game.dice[3]))        die5.setImageDrawable(getDrawableFromString(this, game.dice[4]))        die6.setImageDrawable(getDrawableFromString(this, game.dice[5]))    }    private fun onDiceClick(dieNumber: Int, die: ImageButton) {        // what happens when a dice icon is clicked        if (game.reRollCount == 0) {            return        }        listOfDiceState[dieNumber] = !listOfDiceState[dieNumber]        if (listOfDiceState[dieNumber])            die.setBackgroundColor(Color.DKGRAY)        else            die.setBackgroundColor(Color.LTGRAY)        val rollButton = findViewById<Button>(R.id.roll_button)        if (listOfDiceState.find{ it } == null) {            rollButton.text = "SUBMIT"        }        else {            rollButton.text = "ROLL"        }    }    private fun setGameState() {        if (game.state != "running") {            // TODO: Add function call to move to game over state            if (game.state == "win") {                // TODO: move to victory state            }            else {                // TODO: move to defeat state            }        }        // set the dice        diceUpdate()        //TODO: set characters    }    private fun getDrawableFromString(context: Context, drawableName: String) : Drawable? {        return context.let { ContextCompat.getDrawable(            it, resources.getIdentifier(                drawableName,                "drawable", context.packageName            )        )}    }}