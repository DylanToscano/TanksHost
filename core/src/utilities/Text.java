package utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Text {
	private BitmapFont fuente;
	private float x = 0, y = 0;
	private String texto = "";
	GlyphLayout layout;

	public Text(String rutaFuente, int dimension, Color color, boolean sombra) {

		FreeTypeFontGenerator generador = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
		FreeTypeFontGenerator.FreeTypeFontParameter parametros = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parametros.size = dimension;
		parametros.color = color;
		if (sombra) {
			parametros.shadowColor = Color.BLACK;
			parametros.shadowOffsetX = 1;
			parametros.shadowOffsetY = 1;
		}
		layout = new GlyphLayout();
		fuente = generador.generateFont(parametros);
	}

	public Text(String rutaFuente, float dimension, Color color, boolean sombra) {

		FreeTypeFontGenerator generador = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
		FreeTypeFontGenerator.FreeTypeFontParameter parametros = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parametros.size = (int) (100 * dimension);
		parametros.color = color;
		if (sombra) {
			parametros.shadowColor = Color.BLACK;
			parametros.shadowOffsetX = 1;
			parametros.shadowOffsetY = 1;
		}
		layout = new GlyphLayout();
		fuente = generador.generateFont(parametros);
	}

	public void dibujar() {

		fuente.draw(Render.batch, texto, x, y);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
		layout.setText(fuente, texto);
	}

	public float getAncho() {
		return layout.width;
	}

	public float getAlto() {
		return layout.height;
	}

	public void setColor(Color color) {
		fuente.setColor(color);
	}

}
