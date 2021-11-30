package utilities;

public abstract class Config {
	public static int HEIGHT= 720;
    public static int WIDTH = 1080;
    public static float delta;
    // PIXELPERMETER la escala, un pixel a la imagen, para que las velocidades y otras propiedades tengan sentido
    public static final float PPM = 100;
    
    // filtros utilizando BITWISE:
    public static final short DEFAULT_BIT = 1;
    public static final short TANK_BIT = 2;
    public static final short ROAD_BIT = 4;
    public static final short BUFF_BIT = 8;
    public static final short PROJECTIL_BIT = 16;
    public static final short EXPLOSION_BIT = 32;
    public static final short BARREL_BIT = 64;

   
    // 0000 1100
}
