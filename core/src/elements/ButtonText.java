package elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

import utilities.Render;

public class ButtonText {
	private BitmapFont fuente;
	private float x=0,y=0;
	private String texto= "";
	GlyphLayout layout;
	private Color defaultColor;
	private boolean hovered;
	private boolean pressed;

	public ButtonText(String direction, int dimension, Color color, boolean shadow) {
		defaultColor = color;
		FreeTypeFontGenerator generador = new FreeTypeFontGenerator(Gdx.files.internal(direction));
		FreeTypeFontGenerator.FreeTypeFontParameter parametros = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parametros.size = dimension;
		parametros.color = color;
		if (shadow) {
			parametros.shadowColor = Color.BLACK;
			parametros.shadowOffsetX = 3;
			parametros.shadowOffsetY = 2;
		}
		layout = new GlyphLayout();
		fuente = generador.generateFont(parametros);

	}

	public void draw() {
		fuente.draw(Render.batch, texto, x, y);
	}
	public void update(float x, float y) {
		if(inCoords(x,y)) {
			pressed = true;

			
		}else {
			pressed = false;
		}
	}
	public boolean inCoords(float x, float y) {
		if (x >= this.x && x <= this.x + getWidth()) {
			if (y >= this.y - getHeight() && y <= this.y ) {
				
				return true;
			}
		}
		return false;
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
	public String getText() {
		return texto;
	}
	public void setText(String texto) {
		this.texto = texto;
		layout.setText(fuente, texto);
	}
	public float getWidth() {
		return layout.width;
	}
	public float getHeight() {
		return layout.height;
	}
	public Vector2 getPosition() {
		return new Vector2(layout.width,layout.height);
	}
	public void setColor(Color color){
		fuente.setColor(color);
	}

	public void updateHover(float x, float y) {
		if(inCoords(x, y)) {
			setColor(Color.RED);
			hovered = true;
		}else {
			setColor(defaultColor);
			hovered =false;
		}	
	}
	public boolean isHovered() {
		return hovered;
		
	}
	public boolean isPressed() {
		return pressed;
		
	}
	
}
