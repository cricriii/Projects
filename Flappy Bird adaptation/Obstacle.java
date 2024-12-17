public class Obstacle extends Personnage {

	public static final int NBR_IMAGES = 46;
	public static final int MAX_RAYON = 45;

	private int imageIndex;

	private boolean passed = false;
	private boolean intersecting = false;
	private boolean hasIntersected = false;

	/**
	 * Constructeur des Obstacle
	 *
	 * @param x abscisse de l'obstacle
	 * @param y ordonnnéé de l'obstacle
	 */
	public Obstacle(double x, double y) {
		super(x, y);
		this.imageIndex = (int) (Math.random() * NBR_IMAGES);
	}

	/**
	 * Cette méthode permet d'afficher aléatoirement les obstacles du jeu
	 *
	 * @return l'identifiant de l'image
	 */
	public int getImageIndex() {
		return this.imageIndex;
	}

	/**
	 * Cette méthode permet de générer des rayons aléatoirement pour chaque Obstacle
	 *
	 * @return le rayon de l'obstacle
	 */

	@Override
	public int getRayon() {
		return rayon;
	}

	public void setRayon(int rayon) {
		this.rayon = rayon;
	}

	/**
	 * Déplacer l'obstacle avec une règle par le temps
	 *
	 * @param t Delta temps - s
	 */
	@Override
	public void move(double dt) {
		this.x -= dt * this.vx;
	}

	/**
	 * Si l'obstacle a passé Flappy
	 * Donc ne devrait pas être re-compté
	 */
	public boolean hasPassed() {
		return this.passed;
	}

	/**
	 * Quand il passe Flappy, le marquer et ajouter au score
	 *
	 * @param f Le Flappy (pour ajouter au score)
	 */
	public void pass(Flappy f) {
		this.passed = true;
		f.incrementScore();
	}

	/**
	 * S'il intersecte actuelement
	 */
	public boolean getIntersecting() {
		return this.intersecting;
	}

	/**
	 * S'il a intersecté auparavant
	 */
	public boolean hasIntersected() {
		return this.hasIntersected;
	}

	/**
	 * Actualiser son état d'intersectage
	 * Son état précédent n'est pas modifié
	 */
	public void toggleIntersecting() {
		this.intersecting = !this.intersecting;
		this.hasIntersected = true;
	}
}
