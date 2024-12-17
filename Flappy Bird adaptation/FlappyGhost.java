import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;

public class FlappyGhost extends Application {

	// px
	public static final int MAX_WIDTH = 640;
	public static final int MAX_HEIGHT = 440;
	public static final int GAME_HEIGHT = 400;

	// Controleur
	private Controleur controleur = new Controleur(this);

	// Platform design
	VBox root = new VBox();
	Scene scene = new Scene(root, MAX_WIDTH, MAX_HEIGHT);
	GraphicsContext context;

	// Background scene
	Pane pane = new Pane();

	// Components of the window
	private Button pause = new Button("Pause");
	private CheckBox debug = new CheckBox("Mode debug");
	private Separator[] separator = new Separator[2];
	private Text scoreLabel = new Text("Score : ");
	private Text score = new Text("");

	// Framerate du jeu
	private int fps = 2;

	// Path du dossier image
	private static final String PATH = "images/";

	// Le mode default du debugMode du jeu
	private boolean debugMode = false;

	// Instancier image du fantome
	Image ghost = new Image(PATH + "ghost.png");
	ImageView fantomeView = new ImageView(ghost);
	Circle ghostCercle = new Circle();

	// Animation of the background
	private ImageView backgrounds = { new ImageView(new Image(PATH + "bg.png"))};

	// Obstacles
	private ArrayList<ImageView> obstacles = new ArrayList<ImageView>();
	private ArrayList<Circle> obstaclesCercles = new ArrayList<Circle>();

	// Actions
	AnimationTimer mouvements = new AnimationTimer() {
		private long lastTime = 0; // ns
		private double x = 0, y = 150;

		@Override
		public void start() {
			lastTime = System.nanoTime();
			super.start();
		}

		@Override
		public void handle(long now) {
			double deltaTime = (now - lastTime) * 1e-9; // s
			lastTime = now;

			// Activer déroulement de l'arrière-plan
			controleur.deroulerPlan(deltaTime);

			// Activer la gravité pour Flappy - lui bouger
			controleur.bougerFantome(deltaTime);

			// Déplacer les monstres
			controleur.bougerMonstres(deltaTime);

			// Créer des monstres
			controleur.creerMonstres(deltaTime);

			// Update
			controleur.update();
		}
	};

	private boolean onPause = false;

	@Override
	public void start(Stage primaryStage) throws Exception {

		root.getChildren().add(pane);
		Canvas gameScene = new Canvas(MAX_WIDTH, GAME_HEIGHT);
		pane.getChildren().add(gameScene);
		context = gameScene.getGraphicsContext2D();

		// Arrière-plans
		backgrounds[1].setX(MAX_WIDTH);
		pane.getChildren().addAll(backgrounds);

		// Separator
		for (int i = 0; i < separator.length; i++){
			separator[i] = new Separator();
			separator[i].setOrientation(Orientation.VERTICAL);
			separator[i].setMinHeight(40);
		}

		// Menu
		HBox menu = new HBox(5);
		menu.setAlignment(Pos.CENTER);
		menu.setPadding(new Insets(0, 0, 0, 5));

		menu.getChildren().add(pause);
		menu.getChildren().add(separator[0]);
		menu.getChildren().add(debug);
		menu.getChildren().add(separator[1]);
		menu.getChildren().add(scoreLabel);
		menu.getChildren().add(score);
		root.getChildren().add(1, menu);


		/* Après l’exécution de la fonction, le
		   focus va automatiquement au canvas */
		Platform.runLater(() -> {
			gameScene.requestFocus();
		});

		/* Lorsqu’on clique ailleurs sur la scène,
		   le focus retourne sur le canvas */
		scene.setOnMouseClicked((event) -> {
			gameScene.requestFocus();
		});

		// Threads du jeu
		scene.setOnKeyPressed(e -> {
			// Sortir du jeu
			if (e.getCode() == KeyCode.ESCAPE) {
				Platform.exit();
			}
			// Faire sauter
			if (e.getCode() == KeyCode.SPACE) {
				controleur.sauterFantome();
			}
		});

		// Dernières modifications à la scène
		primaryStage.setTitle("Flappy Ghost");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(ghost);

		primaryStage.setResizable(false);
		primaryStage.show();

		// Pour commencer jeu
		controleur.commencer();

		debug.setOnAction(e -> {
			toggleDebugMode();
		});

		pause.setOnMouseClicked(e -> {
			if (onPause) {
				onPause = false;
				mouvements.start();
			} else {
				onPause = true;
				mouvements.stop();
			}
		});
	}

	/**
	 * Calculer une coordonné par rapport à l'origine
	 * Nécessaire avec le calcul de position different Image/Cercle
	 *
	 * @param c Position par centre du rayon
	 * @param rayon Le rayon du personnage
	 */
	private double getOffset(double c, double rayon) {
		return c - rayon;
	}

	/**
	 * Ajouter image de Flappy
	 *
	 * @param rayon de Flappy (30px)
	 */
	public void initFlappy(int rayon) {
		Platform.runLater(() -> {
			ghostCercle.setRadius(rayon);
			pane.getChildren().addAll(fantomeView, ghostCercle);

			fantomeView.setVisible(!debugMode); ghostCercle.setVisible(debugMode);
			mouvements.start();
		});
	}

	/**
	 * Changer Flappy de position
	 *
	 * @param x Position x par centre du bounding cercle
	 * @param y Position y par centre du bounding cercle
	 */
	public void moveGhost(double x, double y) {
		Platform.runLater(() -> {
			double rayon = ghostCercle.getRadius();
			fantomeView.setX(getOffset(x, rayon)); fantomeView.setY(getOffset(y, rayon));

			ghostCercle.setCenterX(x); ghostCercle.setCenterY(y);
		});
	}

	/**
	 * Changer un obstacle de position
	 *
	 * @param index La position de l'obstacle dans les arraylist
	 * @param x Position x par centre du bounding cercle
	 * @param y Position y par centre du bounding cercle
	 */
	public void moveObstacle(int index, double x, double y) {
		Platform.runLater(() -> {
			ImageView obs = obstacles.get(index);
			Circle obsDebug = obstaclesCercles.get(index);
			double rayon = obsDebug.getRadius();

			obs.setX(getOffset(x, rayon)); obs.setY(getOffset(y, rayon));
			obsDebug.setCenterX(x); obsDebug.setCenterY(y);
		});
	}

	/**
	 * Ajouter obstacle au jeu
	 *
	 * @param x Position x par centre du bounding cercle
	 * @param y Position y par centre du bounding cercle
	 * @param rayon Taille de l'obstacle
	 * @param image Index de l'image à utiliser
	 */
	public void ajouterObstacle(double x, double y, int rayon, int image) {
		Platform.runLater(() -> {
			String imageIndex = Integer.toString(image);
			ImageView obsIcon = new ImageView(new Image(PATH + imageIndex + ".png"));
			Circle obsCercle = new Circle(x, y, rayon, Color.YELLOW);

			obsIcon.setX(getOffset(x, rayon)); obsIcon.setY(getOffset(y, rayon));
			obsIcon.setPreserveRatio(true); obsIcon.setFitWidth(rayon * 2);

			obstacles.add(obsIcon); obstaclesCercles.add(obsCercle);
			obsIcon.setVisible(!debugMode); obsCercle.setVisible(debugMode);

			pane.getChildren().addAll(obsIcon, obsCercle);
		});
	}

	/**
	 * Enlèver obstacle des index
	 *
	 * @param index La position dans les array de l'obstacle
	 */
	public void enleverObstacle(int index) {
		Platform.runLater(() -> {
			obstacles.remove(index); obstaclesCercles.remove(index);
		});
	}

	/**
	 * Colourier obstacles par son statut
	 * S'il intersecte avec Flappy, colourier en rouge
	 * Sinon, jaune (comme défaut)
	 *
	 * @param index L'index de l'obstacle dans les array
	 * @param intersecting S'il intersecte
	 */
	public void colourierIntersection(int index, boolean intersecting) {
		Platform.runLater(() -> {
			Circle obs = obstaclesCercles.get(index);

			if (intersecting) {
				obs.setFill(Color.RED);
			} else {
				obs.setFill(Color.YELLOW);
			}
		});
	}

	/**
	 * Modifier score montrée
	 *
	 * @param newScore Nouveau valeur en String
	 */
	public void changerScore(String newScore) {
		Platform.runLater(() -> {
			score.setText(newScore);
		});
	}

	/**
	 * Mettre en mode debug/normal
	 * Change tous les personnages en cercles colouriés/images
	 */
	public void toggleDebugMode() {
		Platform.runLater(() -> {
			debugMode = !debugMode;
			for (ImageView obs : obstacles) {
				obs.setVisible(!debugMode);
			}

			for (Circle cer : obstaclesCercles) {
				cer.setVisible(debugMode);
			}

			fantomeView.setVisible(!debugMode);
			ghostCercle.setVisible(debugMode);
		});
	}

	/**
	 * Faire déflier l'arrière-plan
	 *
	 * @param dt Delta de temps - s
	 * @param vitesse Vitesse de défilé - px/s
	 */
	public void defilerArrierePlan(double dt, double vitesse) {
		Platform.runLater(() -> {
			for (ImageView bg : backgrounds) {
				bg.setX(bg.getX() - dt * vitesse);

				double egout = bg.getX() + MAX_WIDTH;
				if (egout <= 0) {
					bg.setX(MAX_WIDTH + egout);
				}
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
