public abstract class Personnage {

	protected double x; // Du centre - px
	protected double y; // Du centre - px
	protected int rayon;

	protected static int vx; // Vitesse en x (px/s)
	protected static int vy = 0; // Vitesse en y

	// Gravité px/s^2
	protected static int ax = 0;
	protected static int ay = 500;

	/**
	 * Constructeur du Personnage
	 *
	 * @param x abscisse du Personnage
	 * @param y ordonnée du Personnage
	 */
	public Personnage(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructeur du Personnage
	 *
	 * @param x abscisse du Personnage
	 * @param y ordonnée du Personnage
	 * @param rayon La grandeur du personnage
	 */
	public Personnage(double x, double y, int rayon) {
		this.x = x;
		this.y = y;
		this.rayon = rayon;
	}

	/**
	 * @return le rayon du Personnnage - px
	 */
	public int getRayon() {
		return this.rayon;
	}

	/**
	 * @return l'abscisse - px
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @return l'ordonnée - px
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * @param rayon nouveau rayon - px
	 */
	public void setRayon(int rayon) {
		this.rayon = rayon;
	}

	/**
	 * @param x la nouvelle abscisse - px
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @param y la nouvelle ordonnée - px
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return la vitesse en abscisse - px/s
	 */
	public int getVx() {
		return this.vx;
	}

	/**
	 * @param vx la vitesse en abscisse - px/s
	 */
	public void setVx(int vx) {
		this.vx = vx;
	}

	/**
	 * @return la vitesse en ordonnée - px/s
	 */
	public int getVy() {
		return this.vy;
	}

	/**
	 * @param vy la vitesse en ordonnée - px/s
	 */
	public void setVy(int vy) {
		this.vy = vy;
	}

	/**
	 * @return l'accélération en abscisse - px/s^2
	 */
	public int getAx() {
		return this.ax;
	}

	/**
	 * @param ax l'accélération en ordonnée - px/s^2
	 */
	public void setAx(int ax) {
		this.ax = ax;
	}

	/**
	 * @return l'accélération en ordonnée - px/s^2
	 */
	public int getAy() {
		return this.ay;
	}

	/**
	 * @param ay l'accélération en ordonnée - px/s^2
	 */
	public void setAy(int ay) {
		this.ay = ay;
	}

	/**
	 * Déplacer le personnage avec une règle par le temps
	 *. Ébauche pour types différents
	 *
	 * @param dt Delta de temps - s
	 */
	public void move(double dt) {}

	/**
	 * Méthode qui permet de verifier si deux Personnage s'intersecte
	 *
	 * @param other qui l'autre Personnage avec qui ont vérfie l'intersection
	 * @return un booléen qui vérifie la collision
	 */
	public boolean intersect(Personnage other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		double dCarre = Math.pow(dx, 2) + Math.pow(dy, 2);

		return dCarre < Math.pow(this.rayon + other.rayon, 2);
	}
}
