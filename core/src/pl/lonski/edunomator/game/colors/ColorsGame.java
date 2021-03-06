package pl.lonski.edunomator.game.colors;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;

import pl.lonski.edunomator.Edunomator;
import pl.lonski.edunomator.common.Speaker;
import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.GameStage;

public class ColorsGame implements Game {

	private final Edunomator edunomator;
	private GameStage stage;
	private Speaker.Provider speakerProvider;
	private Speaker speaker;
	private List<Brush> brushes;
	private List<Figure> figures;

	public ColorsGame(Edunomator edunomator) {
		this.speakerProvider = edunomator.getSpeakerProvider();
		this.edunomator = edunomator;
	}

	List<Brush> getBrushes() {
		return brushes;
	}

	Speaker getSpeaker() {
		return speaker;
	}

	@Override
	public Game start(String lang) {
		Config config = new Json().fromJson(Config.class,
				Gdx.files.internal("colors/config/" + lang + ".json").readString());

		brushes = new ArrayList<>();
		for (Config.BrushDef def : config.brushes) {
			TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("colors/" + def.textureFile)));
			brushes.add(new Brush(def.id, def.spokenName, texture));
		}

		figures = new ArrayList<>();
		for (Config.FigureDef def : config.figures) {
			TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("colors/" + def.textureFile)));
			TextureRegion textureColor =
					new TextureRegion(new Texture(Gdx.files.internal("colors/" + def.textureFileColor)));
			figures.add(new Figure(def.brushId, def.spokenName, texture, textureColor));
		}
		Collections.shuffle(figures);

		speaker = speakerProvider.get(new Locale(config.language));
		stage = new PlayStage(this);
		edunomator.setInputListener(stage);
		return this;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public List<Figure> getFigures() {
		return figures;
	}
}

