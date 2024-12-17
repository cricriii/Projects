public class ObstacleSinus extends Obstacle {

	public static final int AMPLITUDE = 50; // px

	private double yInitial; // px

	public ObstacleSinus(double x, double y) {
		super(x, y);
		this.yInitial = y;
	}

	/**
	 * Déplacer le personnage avec une onde sinus
	 *
	 * @param dt Delta de temps - s
	 */
	@Override
	public void move(double dt) {
		super.move(dt);
		this.y = AMPLITUDE / 2 * Math.sin(this.x / AMPLITUDE * 2) + this.yInitial;
	}
}
