package io.github.patrickbelanger.game.core.screens.menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.patrickbelanger.game.core.GameAssetManager;
import io.github.patrickbelanger.groq.dto.GroqResponseDTO;

public class CharacterSelectionMenu extends BaseScreen {
    private GameAssetManager assetManager;
    private BitmapFont font;
    private TextureRegion characterSelectionBackground;
    private TextureRegion characterSelectionBackgroundInactive;
    private TextureRegion characterSelectionPanel;
    private TextureRegion characterFemaleSprite;
    private TextureRegion characterMaleSprite;
    private TextureRegion characterFemaleInactiveSprite;
    private TextureRegion characterMaleInactiveSprite;
    private SpriteBatch batch;
    private GroqResponseDTO responseDTO;
    private String dialogueCharacterFemale;
    private String dialogueCharacterMale;
    private String dialogueIntroductionPanel;
    private boolean closeIntroductionPanel = false;
    private boolean characterSelected = false;
    private boolean characterFemaleInactive = false;
    private boolean characterMaleInactive = false;

    /* Animation (temporary logic - need to extract it and make it more generic) */
    private float maleCharacterX = 440;
    private float defaultMaleCharacterX = 440;
    private boolean startMaleExitAnimation = false;
    private boolean startMaleReturnAnimation = false;

    private float femaleCharacterX = 66;
    private final float defaultFemaleCharacterX = 66;
    private boolean startFemaleExitAnimation = false;
    private boolean startFemaleReturnAnimation = false;

    private float exitSpeed = 500f;
    private float returnSpeed = 500f;
    private boolean disableInput = false;

    public CharacterSelectionMenu(Game game) {
        super(game);
    }

    @Override
    public void show() {
        assetManager = new GameAssetManager();
        batch = new SpriteBatch();

        assetManager.loadFont("minecraft", "Minecraft.ttf", 16);
        assetManager.loadTexture("character-selection.png");
        assetManager.loadTexture("character-selection-inactive.png");
        assetManager.loadTexture("character-selection-panel.png");
        assetManager.loadTexture("character-male-active.png");
        assetManager.loadTexture("character-male-inactive.png");
        assetManager.loadTexture("character-female-active.png");
        assetManager.loadTexture("character-female-inactive.png");
        assetManager.finishLoading();

        characterSelectionBackground = new TextureRegion(assetManager.getTexture("character-selection.png"), 0, 0, 800, 600);
        characterSelectionBackgroundInactive = new TextureRegion(assetManager.getTexture("character-selection-inactive.png"), 0, 0, 800, 600);
        characterSelectionPanel = new TextureRegion(assetManager.getTexture("character-selection-panel.png"), 0, 0, 699, 124);
        characterFemaleSprite = new TextureRegion(assetManager.getTexture("character-female-active.png"), 0, 0, 330, 550);
        characterMaleSprite = new TextureRegion(assetManager.getTexture("character-male-active.png"), 0, 0, 330, 550);
        characterFemaleInactiveSprite = new TextureRegion(assetManager.getTexture("character-female-inactive.png"), 0, 0, 330, 550);
        characterMaleInactiveSprite = new TextureRegion(assetManager.getTexture("character-male-inactive.png"), 0, 0, 330, 550);

        font = assetManager.getFont("minecraft");

        batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 600);

        dialogueIntroductionPanel = "Brave adventurer, the whispers of the Ancient Kingdom beckon! Choose your champion: Lyra, the cunning rogue, or Thorin, the fearless warrior. Embark on a legendary quest, where valor and wit shall be tested. The fate of the realm awaits.";
        dialogueCharacterFemale = "Lyra, once a skilled thief in the city of Whisperhaven, fled after uncovering a web of corruption within the merchant guild. Now, she's driven by a desire for redemption and truth, seeking the lost city of Elyria to conquer the darkness that has consumed her past.";
        dialogueCharacterMale = "Thorin, son of the great warrior, Eirak. Born in the mountains, he was forged in the fire of battle. His people, the Kaelin, were once a mighty tribe, but their lands were ravaged by the dark sorcerer, Malakar. Thorin's family was slaughtered, and he was left for dead. He survived, driven by a burning rage and a thirst for revenge against Malakar.";
    }


    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(characterSelectionBackground, 0, 0);
        batch.draw(closeIntroductionPanel || characterSelected
                ? characterSelectionBackgroundInactive : characterSelectionBackground, 0, 0
        );
        batch.draw(characterFemaleInactive ? characterFemaleInactiveSprite : characterFemaleSprite, femaleCharacterX, -80);
        batch.draw(characterMaleInactive ? characterMaleInactiveSprite : characterMaleSprite, maleCharacterX, -50);

        /* Animations - Section to be refactored */
        // Update male character's position for exit animation
        if (startMaleExitAnimation) {
            disableInput = true;
            if (maleCharacterX > Gdx.graphics.getWidth()) {
                startMaleExitAnimation = false;
                disableInput = false;
            } else {
                maleCharacterX += exitSpeed * v;
            }
        }

        // Update male character's position for return animation
        if (startMaleReturnAnimation) {
            disableInput = true;
            if (maleCharacterX <= defaultMaleCharacterX) {
                maleCharacterX = defaultMaleCharacterX;
                startMaleReturnAnimation = false;
                disableInput = false;
            } else {
                maleCharacterX -= returnSpeed * v;
            }
        }

        // Update female character's position for exit animation
        if (startFemaleExitAnimation) {
            disableInput = true;
            if (femaleCharacterX < -characterFemaleInactiveSprite.getRegionWidth()) {
                startFemaleExitAnimation = false;
                disableInput = false;
            } else {
                femaleCharacterX -= exitSpeed * v;
            }
        }

        // Update female character's position for return animation
        if (startFemaleReturnAnimation) {
            disableInput = true;
            if (femaleCharacterX >= defaultFemaleCharacterX) {
                femaleCharacterX = defaultFemaleCharacterX;
                startFemaleReturnAnimation = false;
                disableInput = false;
            } else {
                femaleCharacterX += returnSpeed * v;
            }
        }

        if (!closeIntroductionPanel) {
            displayIntroPanel(v);
        } else if (!characterSelected){
            displayChooseCharacter(v);
        } else {
            displayCharacterBackstory(v);
        }

        batch.end();
    }

    private void displayIntroPanel(float delta) {
        drawTextPanel(
            delta,
            batch,
            characterSelectionPanel,
            font,
            dialogueIntroductionPanel, () -> {
                closeIntroductionPanel = true;
                characterMaleInactive = true; // Want to highlight the default character (female)
            }
        );
    }

    private void displayChooseCharacter(float delta) {
        timeSinceLastInput += delta;

        font.setColor(Color.WHITE);
        font.draw(batch, "Use A/D or LEFT/RIGHT to select, Enter to confirm", 210, 38);

        if (timeSinceLastInput >= INPUT_DELAY) {
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isKeyPressed(Input.Keys.A))) && (closeIntroductionPanel)) {
                timeSinceLastInput= 0;
                characterFemaleInactive = false;
                characterMaleInactive = true;
            } else if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isKeyPressed(Input.Keys.D))) && (closeIntroductionPanel)) {
                timeSinceLastInput= 0;
                characterFemaleInactive = true;
                characterMaleInactive = false;
            } else if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                timeSinceLastInput= 0;
                characterSelected = true;
            }
        }
    }

    private void displayCharacterBackstory(float delta) {
        drawTextPanel(
            delta,
            batch,
            characterSelectionPanel,
            font,
            characterMaleInactive ? dialogueCharacterFemale : dialogueCharacterMale, () -> {
                // To define
            },
            false
        );
        font.setColor(Color.WHITE);
        font.draw(batch, "ESC to select another character, Enter to confirm", 215, 38);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && (!disableInput)) {
            timeSinceLastInput= 0;
            characterSelected = false;

            if (characterFemaleInactive) {
                startFemaleReturnAnimation = true;
            } else if (characterMaleInactive) {
                startMaleReturnAnimation = true;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && (!disableInput)) {
            timeSinceLastInput= 0;
            characterSelected = true;
            if (characterFemaleInactive) {
                startFemaleExitAnimation = true;
            } else if (characterMaleInactive) {
                startMaleExitAnimation = true;
            }
        }
    }

    @Override
    public void hide() {
        Gdx.app.debug("EOTAK", "dispose character selection menu");
        batch.dispose();
        characterSelectionBackground.getTexture().dispose();
        characterSelectionBackgroundInactive.getTexture().dispose();
        characterSelectionPanel.getTexture().dispose();
        characterFemaleSprite.getTexture().dispose();
        characterMaleSprite.getTexture().dispose();
        characterFemaleInactiveSprite.getTexture().dispose();
        characterMaleInactiveSprite.getTexture().dispose();
        assetManager.dispose();
    }
}
