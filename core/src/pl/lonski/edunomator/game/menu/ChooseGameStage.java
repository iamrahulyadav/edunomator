package pl.lonski.edunomator.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import pl.lonski.edunomator.TextureActor;
import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.GameStage;

class ChooseGameStage extends GameStage {

	private final GameMenu menu;

	ChooseGameStage(final GameMenu menu) {
		super(0.5f);
		this.menu = menu;

		TextureActor colorsBtn = new TextureActor(new Texture(Gdx.files.internal("game_icons/colors.png")));
		colorsBtn.setPosition(
				getScreenWidth() / 4 - colorsBtn.getWidth() / 2,
				getScreenHeight() / 2 - colorsBtn.getHeight() / 2
		);
		colorsBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.start(Game.Type.COLORS_GAME);
				return true;
			}
		});
		addActor(colorsBtn);

		TextureActor numbersBtn = new TextureActor(new Texture(Gdx.files.internal("game_icons/numbers.png")));
		numbersBtn.setPosition(
				colorsBtn.getX() + colorsBtn.getWidth() * 1.2f,
				getScreenHeight() / 2 - numbersBtn.getHeight() / 2
		);
		numbersBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.start(Game.Type.NUMBERS_GAME);
				return true;
			}
		});
		addActor(numbersBtn);

		TextureActor wordsBtn = new TextureActor(new Texture(Gdx.files.internal("game_icons/words.png")));
		wordsBtn.setPosition(
				numbersBtn.getX() + numbersBtn.getWidth() * 1.2f,
				getScreenHeight() / 2 - wordsBtn.getHeight() / 2
		);
		wordsBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.start(Game.Type.WORDS_GAME);
				return true;
			}
		});
		addActor(wordsBtn);
	}
}
