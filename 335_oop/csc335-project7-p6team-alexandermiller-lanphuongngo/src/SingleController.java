import javafx.scene.layout.TilePane;

public class SingleController extends Connect4Controller{
	private Human human;
	private Computer comp;
	private TilePane game;
	public SingleController(TilePane game) {
		super();
		this.game = game;
		comp = new Computer(2,model);
		human = new Human(1,game,model,this,comp);
	}
	
	public SingleController(TilePane game, Connect4Model model) {
		super(model);
		this.game = game;
		comp = new Computer(2,model);
		human = new Human(1,game,model,this,comp);
	}
	
	public void move() {
		human.setDone(false);
	}
}
