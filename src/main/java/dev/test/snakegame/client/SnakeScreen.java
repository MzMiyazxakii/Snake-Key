package dev.test.snakegame.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeScreen extends Screen {
    private static final int COLS=22, ROWS=16, CELL=12, BOARD_W=COLS*CELL, BOARD_H=ROWS*CELL;
    private final Random random=new Random();
    private final List<CellPos> snake=new ArrayList<>();
    private CellPos food;
    private int dx=1, dy=0, queuedDx=1, queuedDy=0, ticks, score;
    private boolean gameOver;

    public SnakeScreen(){ super(Component.literal("Змейка")); resetGame(); }
    private void resetGame(){
        snake.clear(); snake.add(new CellPos(COLS/2,ROWS/2)); snake.add(new CellPos(COLS/2-1,ROWS/2)); snake.add(new CellPos(COLS/2-2,ROWS/2));
        dx=queuedDx=1; dy=queuedDy=0; score=0; ticks=0; gameOver=false; placeFood();
    }
    private void placeFood(){ do{ food=new CellPos(random.nextInt(COLS),random.nextInt(ROWS)); }while(snake.contains(food)); }
    @Override public void tick(){ super.tick(); if(gameOver)return; if(++ticks>=5){ticks=0;moveSnake();} }
    private void playDeathSound(){
        SoundEvent death=SoundEvent.createVariableRangeEvent(new ResourceLocation("snakegame","death"));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(death,1.0F));
    }
    private void moveSnake(){
        dx=queuedDx; dy=queuedDy; CellPos head=snake.get(0); CellPos next=new CellPos(head.x+dx,head.y+dy);
        boolean eating=next.equals(food);
        if(next.x<0||next.x>=COLS||next.y<0||next.y>=ROWS){gameOver=true;playDeathSound();return;}
        int limit=eating?snake.size():snake.size()-1;
        for(int i=0;i<limit;i++)if(snake.get(i).equals(next)){gameOver=true;playDeathSound();return;}
        snake.add(0,next); if(eating){score++;placeFood();}else snake.remove(snake.size()-1);
    }
    @Override public boolean keyPressed(int keyCode,int scanCode,int modifiers){
        if(keyCode==GLFW.GLFW_KEY_R&&gameOver){resetGame();return true;}
        if(keyCode==GLFW.GLFW_KEY_UP||keyCode==GLFW.GLFW_KEY_W){setDirection(0,-1);return true;}
        if(keyCode==GLFW.GLFW_KEY_DOWN||keyCode==GLFW.GLFW_KEY_S){setDirection(0,1);return true;}
        if(keyCode==GLFW.GLFW_KEY_LEFT||keyCode==GLFW.GLFW_KEY_A){setDirection(-1,0);return true;}
        if(keyCode==GLFW.GLFW_KEY_RIGHT||keyCode==GLFW.GLFW_KEY_D){setDirection(1,0);return true;}
        return super.keyPressed(keyCode,scanCode,modifiers);
    }
    private void setDirection(int x,int y){if(x==-dx&&y==-dy)return;queuedDx=x;queuedDy=y;}
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick){
        renderBackground(g); int left=(width-BOARD_W)/2, top=(height-BOARD_H)/2;
        g.drawCenteredString(font,"ЗМЕЙКА",width/2,top-42,0xFFFFFF); g.drawCenteredString(font,"Счёт: "+score,width/2,top-27,0xFFFF55);
        g.fill(left-2,top-2,left+BOARD_W+2,top+BOARD_H+2,0xFFB8B8B8); g.fill(left,top,left+BOARD_W,top+BOARD_H,0xFF202830);
        for(int x=0;x<=COLS;x++)g.fill(left+x*CELL,top,left+x*CELL+1,top+BOARD_H,0xFF303B45);
        for(int y=0;y<=ROWS;y++)g.fill(left,top+y*CELL,left+BOARD_W,top+y*CELL+1,0xFF303B45);
        drawCell(g,left,top,food,0xFFE34B4B);
        for(int i=snake.size()-1;i>=0;i--)drawCell(g,left,top,snake.get(i),i==0?0xFF9CFF55:0xFF42B84A);
        if(gameOver){g.fill(left,top,left+BOARD_W,top+BOARD_H,0x99000000);g.drawCenteredString(font,"ИГРА ОКОНЧЕНА",width/2,top+BOARD_H/2-12,0xFFFF5555);g.drawCenteredString(font,"Нажми R, чтобы начать заново",width/2,top+BOARD_H/2+4,0xFFFFFFFF);}
        g.drawCenteredString(font,"Стрелки / WASD — движение   •   Esc — закрыть",width/2,top+BOARD_H+12,0xFFFFFF);
        super.render(g,mouseX,mouseY,partialTick);
    }
    private void drawCell(GuiGraphics g,int left,int top,CellPos p,int color){int x=left+p.x*CELL,y=top+p.y*CELL;g.fill(x+1,y+1,x+CELL-1,y+CELL-1,color);}
    @Override public boolean isPauseScreen(){return false;}
    private record CellPos(int x,int y){}
}