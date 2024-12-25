import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MinesweeperGUI extends JFrame {
    private static final int BOARD_SIZE = 10; // 10x10 grid
    private static final int NUM_MINES = 15;
    private JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private boolean[][] mines = new boolean[BOARD_SIZE][BOARD_SIZE];
    private boolean[][] revealed = new boolean[BOARD_SIZE][BOARD_SIZE];

    public MinesweeperGUI() {
        // Set up the JFrame
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        setSize(600, 600);

        // Initialize the board
        initializeBoard();

        // Place mines
        placeMines();

        // Display the frame
        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = new JButton();
                buttons[row][col] = button;
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.addActionListener(new CellClickListener(row, col));
                add(button); // Add button to the grid layout
            }
        }
    }

    private void placeMines() {
        Random rand = new Random();
        int placedMines = 0;

        while (placedMines < NUM_MINES) {
            int row = rand.nextInt(BOARD_SIZE);
            int col = rand.nextInt(BOARD_SIZE);

            if (!mines[row][col]) {
                mines[row][col] = true;
                placedMines++;
            }
        }
    }

    private void revealCell(int row, int col) {
        if (row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE || revealed[row][col]) {
            return;
        }

        revealed[row][col] = true;

        if (mines[row][col]) {
            buttons[row][col].setText("💣");
            buttons[row][col].setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, "Game Over! You hit a mine.");
            resetGame();
            return;
        }

        int adjacentMines = countAdjacentMines(row, col);
        buttons[row][col].setEnabled(false);
        if (adjacentMines > 0) {
            buttons[row][col].setText(String.valueOf(adjacentMines));
        } else {
            buttons[row][col].setText("");
            buttons[row][col].setBackground(Color.LIGHT_GRAY);

            // Recursively reveal adjacent cells
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr != 0 || dc != 0) {
                        revealCell(row + dr, col + dc);
                    }
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newCol = col + dc;

                if (newRow >= 0 && newCol >= 0 && newRow < BOARD_SIZE && newCol < BOARD_SIZE && mines[newRow][newCol]) {
                    count++;
                }
            }
        }

        return count;
    }

    private void resetGame() {
        dispose(); // Close the current window
        new MinesweeperGUI(); // Start a new game
    }

    private class CellClickListener implements ActionListener {
        private int row;
        private int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            revealCell(row, col);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperGUI::new);
    }
}
