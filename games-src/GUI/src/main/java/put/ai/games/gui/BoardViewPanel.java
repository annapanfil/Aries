/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import put.ai.games.Point;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.MoveMove;
import put.ai.games.game.moves.PlaceAndRotateMove;
import put.ai.games.game.moves.PlaceMove;
import put.ai.games.game.moves.SkipMove;
import put.ai.games.game.moves.impl.MoveMoveImpl;
import put.ai.games.game.moves.impl.PlaceMoveImpl;

public class BoardViewPanel extends JPanel {

    private Board board;
    private Buffer lastMoves = new CircularFifoBuffer(2);
    private Image white, black, skip;
    private Image scaledWhite, scaledBlack, scaledSkip;
    private int previous_d;


    private Color getColor(Player.Color player, float alpha) {
        switch (player) {
            default:
            case EMPTY:
                return new Color(0.5f, 0.5f, 0.5f, alpha);
            case PLAYER1:
                return new Color(1, 200.0f / 255, 0, alpha);
            case PLAYER2:
                return new Color(0, 0, 1, alpha);
        }
    }


    private Color getColor(Player.Color player) {
        return getColor(player, 1);
    }


    private void drawPlaceMove(Graphics g, PlaceMove pm, int d) {
        g.setColor(Color.GREEN);
        g.drawRect(pm.getX() * d, pm.getY() * d, d, d);
    }


    private void drawSkipMove(Graphics g, SkipMove pm, int d) {
        g.setColor(Color.GREEN);
        Point pos = getSkipButtonPosition();
        g.drawRect(pos.getX() * d, pos.getY() * d, d, d);
    }


    private void drawMoveMove(Graphics g, MoveMove mm, int d) {
        //            g.setColor(new Color(1, 0, 0, 0.25f));
        //            g.fillRect(mm.getSrcX() * d, mm.getSrcY() * d, d, d);
        //            g.setColor(new Color(0, 1, 0, 0.25f));
        //            g.fillRect(mm.getDstX() * d, mm.getDstY() * d, d, d);
        g.setColor(getColor(mm.getColor(), 0.5f));
        Direction dir = drawLine(mm, d, g);
        drawArrow(dir, mm, d, g);
    }

    private void drawPlaceAndRotateMove(Graphics g, PlaceAndRotateMove m, int d) {
        drawPlaceMove(g, new PlaceMoveImpl(m.getPlaceX(), m.getPlaceY(), m.getColor()), d);
        drawMoveMove(g, new MoveMoveImpl(m.getRotateSrcX(), m.getRotateSrcY(), m.getRotateDstX(), m.getRotateDstY(), m.getColor()), d);
    }


    private void drawArrow(Direction dir, MoveMove mm, int d, Graphics g) {
        Polygon poly;
        double[][] arrow = new double[][]{{0, -1}, {Math.sqrt(2), 0}, {0, 1}}; //east
        if (dir == Direction.SOUTH || dir == Direction.NORTH) {
            for (int i = 0; i < arrow.length; ++i) {
                double x = arrow[i][0];
                arrow[i][0] = arrow[i][1];
                arrow[i][1] = x;
            }
            if (dir == Direction.NORTH) {
                for (int i = 0; i < arrow.length; ++i) {
                    arrow[i][1] = -arrow[i][1];
                }
            }
        }
        if (dir == Direction.WEST) {
            for (int i = 0; i < arrow.length; ++i) {
                arrow[i][0] = -arrow[i][0];
            }
        }
        if (dir != Direction.UNDEFINED) {
            poly = new Polygon();
            for (double[] p : arrow) {
                int x = mm.getDstX() * d + d / 2;
                int y = mm.getDstY() * d + d / 2;
                poly.addPoint(x + (int) (p[0] * d / 5), y + (int) (p[1] * d / 5));
            }
            g.fillPolygon(poly);
        }
    }


    private Direction drawLine(MoveMove mm, int d, Graphics g) {
        Direction dir = Direction.UNDEFINED;
        Polygon poly = new Polygon();
        if (mm.getSrcY() < mm.getDstY()) {
            dir = Direction.SOUTH;
            vertLine(poly, mm.getSrcX(), mm.getSrcY(), mm.getDstY(), d);
        } else if (mm.getSrcY() > mm.getDstY()) {
            dir = Direction.NORTH;
            vertLine(poly, mm.getSrcX(), mm.getDstY(), mm.getSrcY(), d);
        }
        if (poly.npoints > 0) {
            g.fillPolygon(poly);
            poly = new Polygon();
        }
        if (mm.getSrcX() < mm.getDstX()) {
            dir = Direction.EAST;
            horizLine(poly, mm.getSrcX(), mm.getDstX(), mm.getDstY(), d);
        } else if (mm.getSrcX() > mm.getDstX()) {
            dir = Direction.WEST;
            horizLine(poly, mm.getDstX(), mm.getSrcX(), mm.getDstY(), d);
        }
        if (poly.npoints > 0) {
            g.fillPolygon(poly);
        }
        return dir;
    }


    private enum Direction {

        UNDEFINED,
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    ;


    public BoardViewPanel() {
        try {
            //InputStream stream = BoardViewPanel.class.getResourceAsStream("/put/ai/snort/snortgui/images/white.png");
            InputStream stream = BoardViewPanel.class
                    .getResourceAsStream("/put/ai/snort/snortgui/images/aries-red.png");
            white = javax.imageio.ImageIO.read(stream);
            //stream = BoardViewPanel.class.getResourceAsStream("/put/ai/snort/snortgui/images/black.png");
            stream = BoardViewPanel.class.getResourceAsStream("/put/ai/snort/snortgui/images/aries-blue.png");
            black = javax.imageio.ImageIO.read(stream);
            stream = BoardViewPanel.class.getResourceAsStream("/put/ai/snort/snortgui/images/skip.png");
            skip = javax.imageio.ImageIO.read(stream);
        } catch (IOException ex) {
            Logger.getLogger(BoardViewPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    public void updateBoard(Board b, Move m) {
        this.board = b;
        if (m != null) {
            lastMoves.add(m);
        }
        repaint();
    }


    private Image getImage(int x, int y) {
        switch (board.getState(x, y)) {
            case PLAYER1:
                return scaledWhite;
            case PLAYER2:
                return scaledBlack;
            default:
                return null;
        }
    }


    private int getSide() {
        return Math.min(this.getWidth(), this.getHeight()) / (board.getSize() + 1);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (board == null) {
            return;
        }
        int d = getSide();
        if (d != previous_d) {
            previous_d = d;
            scaledWhite = null;
            scaledBlack = null;
            scaledSkip = null;
        }
        if (scaledWhite == null || scaledBlack == null) {
            scaledWhite = white.getScaledInstance(d - 1, d - 1, Image.SCALE_SMOOTH);
            scaledBlack = black.getScaledInstance(d - 1, d - 1, Image.SCALE_SMOOTH);
            scaledSkip = skip.getScaledInstance(d - 1, d - 1, Image.SCALE_SMOOTH);
        }
        g.setColor(Color.GRAY);
        for (int i = 0; i <= board.getSize(); ++i) {
            g.drawLine(i * d, 0, i * d, board.getSize() * d);
            g.drawLine(0, i * d, board.getSize() * d, i * d);
        }
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < board.getSize(); ++x) {
            for (int y = 0; y < board.getSize(); ++y) {
                g.fillRect(x * d + 1, y * d + 1, d - 1, d - 1);
                g.drawImage(getImage(x, y), x * d + 1, y * d + 1, null);
            }
        }
        {
            Point pos = getSkipButtonPosition();
            g.fillRect(pos.getX() * d + 1, pos.getY() * d + 1, d - 1, d - 1);
            g.drawImage(scaledSkip, pos.getX() * d + 1, pos.getY() * d + 1, null);
        }
        for (Object m : lastMoves) {
            drawMove((Move) m, g, d);
        }
    }

    public Point getSkipButtonPosition() {
        return new Point(this.board.getSize(), 0);
    }


    private void vertLine(Polygon p, int x, int y1, int y2, int d) {
        int w = d / 10;
        assert y1 < y2;
        p.addPoint(x * d + d / 2 + w, y1 * d + d / 2 - w);
        p.addPoint(x * d + d / 2 - w, y1 * d + d / 2 - w);
        p.addPoint(x * d + d / 2 - w, y2 * d + d / 2 + w);
        p.addPoint(x * d + d / 2 + w, y2 * d + d / 2 + w);
    }


    private void horizLine(Polygon p, int x1, int x2, int y, int d) {
        int w = d / 10;
        assert x1 < x2;
        p.addPoint(x1 * d + d / 2 - w, y * d + d / 2 + w);
        p.addPoint(x1 * d + d / 2 - w, y * d + d / 2 - w);
        p.addPoint(x2 * d + d / 2 + w, y * d + d / 2 - w);
        p.addPoint(x2 * d + d / 2 + w, y * d + d / 2 + w);
    }


    private void drawMove(Move m, Graphics g, int d) {
        if (m instanceof PlaceMove) {
            drawPlaceMove(g, (PlaceMove) m, d);
        }
        if (m instanceof MoveMove) {
            drawMoveMove(g, (MoveMove) m, d);
        }
        if (m instanceof SkipMove) {
            drawSkipMove(g, (SkipMove) m, d);
        }
        if (m instanceof PlaceAndRotateMove) {
            drawPlaceAndRotateMove(g, (PlaceAndRotateMove) m, d);
        }
    }


    private class TranslatingMouseListener implements MouseListener {

        private MouseListener base;


        public TranslatingMouseListener(MouseListener base) {
            this.base = base;
        }


        public MouseListener getBase() {
            return base;
        }


        private MouseEvent translate(MouseEvent e) {
            if (board == null) {
                return null;
            }
            e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(),
                    e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            int x = e.getX() / getSide();
            int y = e.getY() / getSide();
            if (x < 0 || y < 0 || x >= board.getSize() + 1 || y >= board.getSize()) {
                return null;
            }
            e.translatePoint(x - e.getX(), y - e.getY());
            return e;
        }


        @Override
        public void mouseClicked(MouseEvent e) {
            e = translate(e);
            if (e != null) {
                base.mouseClicked(e);
            }
        }


        @Override
        public void mousePressed(MouseEvent e) {
            e = translate(e);
            if (e != null) {
                base.mousePressed(e);
            }
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            e = translate(e);
            if (e != null) {
                base.mouseReleased(e);
            }
        }


        @Override
        public void mouseEntered(MouseEvent e) {
            e = translate(e);
            if (e != null) {
                base.mouseEntered(e);
            }
        }


        @Override
        public void mouseExited(MouseEvent e) {
            e = translate(e);
            if (e != null) {
                base.mouseExited(e);
            }
        }
    }


    private List<TranslatingMouseListener> fieldMouseListeners = new ArrayList<>();


    public void addFieldMouseListener(MouseListener listener) {
        TranslatingMouseListener t = new TranslatingMouseListener(listener);
        addMouseListener(t);
        fieldMouseListeners.add(t);
    }


    public void clearFieldMouseListeners() {
        for (TranslatingMouseListener t : fieldMouseListeners) {
            removeMouseListener(t.getBase());
        }
        fieldMouseListeners.clear();
    }
}
