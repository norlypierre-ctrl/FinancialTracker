package com.pluralsight;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class TitleScreen {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Personal Financial ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // Set the window size

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.BLACK);


        JLabel titleLabel = new JLabel("Personal Financial Tracker", SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLUE); // Set text color
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(titleLabel);

        titlePanel.add(Box.createRigidArea(new Dimension(0, 50)));


        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("Ready to Save.");
                JOptionPane.showMessageDialog(frame, "Starting!!!");
            }
        });
        titlePanel.add(startButton);
        titlePanel.add(Box.createVerticalGlue());

        frame.add(titlePanel);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}