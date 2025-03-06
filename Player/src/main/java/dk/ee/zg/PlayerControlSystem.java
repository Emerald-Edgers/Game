package dk.ee.zg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.GameKey;
import dk.ee.zg.common.data.KeyAction;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.World;
import dk.ee.zg.common.map.services.IEntityProcessService;

import java.util.ServiceLoader;

public class PlayerControlSystem implements IEntityProcessService {

    private enum MoveDirection {
        NONE,
        LEFT,
        RIGHT,
        UP,
        DOWN,
    }

    private MoveDirection moveDirection = MoveDirection.NONE;


    public PlayerControlSystem(){

    }

    @Override
    public void process(World world) {
        GameData gameData = GameData.getInstance();
        moveDirection = MoveDirection.NONE;


        if(gameData.getGameKey().isDown(gameData.getGameKey().getActionToKey().get(KeyAction.MOVE_LEFT))){
            moveDirection = MoveDirection.LEFT;
        }
        if(gameData.getGameKey().isDown(gameData.getGameKey().getActionToKey().get(KeyAction.MOVE_RIGHT))){
            moveDirection = MoveDirection.RIGHT;
        }
        if(gameData.getGameKey().isDown(gameData.getGameKey().getActionToKey().get(KeyAction.MOVE_UP))){
            moveDirection = MoveDirection.UP;
        }
        if(gameData.getGameKey().isDown(gameData.getGameKey().getActionToKey().get(KeyAction.MOVE_DOWN))){
            moveDirection = MoveDirection.DOWN;
        }

        for (Entity player : world.getEntities(Player.class)){
            move((Player) player);
        }

    }

    private void move(Player player){
        Vector2 vec = player.getPosition();
        Vector2 dirVec = new Vector2(0,0);
        if (moveDirection == MoveDirection.LEFT) {
            dirVec.add(-1, 0);
        }
        if (moveDirection == MoveDirection.RIGHT) {
            dirVec.add(1,0);
        }
        if (moveDirection == MoveDirection.UP) {
            dirVec.add(0,1);
        }
        if (moveDirection == MoveDirection.DOWN) {
            dirVec.add(0,-1);
        }


        dirVec.nor(); // normalize if diagonal to get no speed boost


        vec.add(dirVec.scl(player.getMoveSpeed() * Gdx.graphics.getDeltaTime()));


        player.setPosition(vec);
    }
}
