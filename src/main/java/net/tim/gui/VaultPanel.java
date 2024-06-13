package net.tim.gui;

import net.tim.EncryptionService;
import net.tim.User;
import net.tim.UserManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class VaultPanel extends JPanel {
    private final String username;
    private final Frame frame;
    private final JTree fileTree;
    private JPasswordField passwordField;

    public VaultPanel(Frame frame, String username) {
        this.username = username;
        this.frame = frame;
        setLayout(new BorderLayout());

        fileTree = setupFileTree(username);
        add(new JScrollPane(fileTree), BorderLayout.CENTER);

        JPanel dropPanel = setupDropPanel();
        add(dropPanel, BorderLayout.EAST);

        JPanel topPanel = setupTopPanel();
        add(topPanel, BorderLayout.NORTH);
    }

    private JTree setupFileTree(String username) {
        File userDirectory = Paths.get("src/main/resources/vault", username).toFile();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(userDirectory);
        createNodes(root, userDirectory);
        JTree fileTree = new JTree(new DefaultTreeModel(root));
        fileTree.addMouseListener(new FileTreeMouseListener());
        return fileTree;
    }

    private JPanel setupDropPanel() {
        JPanel dropPanel = new JPanel();
        dropPanel.setPreferredSize(new Dimension(200, getHeight()));
        dropPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        dropPanel.setBackground(Color.LIGHT_GRAY);
        dropPanel.add(new JLabel("Drag and drop files here to upload"));
        dropPanel.setDropTarget(new FileDropTarget());
        return dropPanel;
    }

    private JPanel setupTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        passwordField = new JPasswordField(20);
        topPanel.add(passwordField);
        JButton settingsButton = new JButton("Settings");
        topPanel.add(settingsButton);
        settingsButton.addActionListener(e -> frame.initializeSettingsPanel(username));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton);
        return topPanel;
    }

    private void logout() {
        frame.remove(this);
        frame.switchPanel("loginPanel");
    }

    private void createNodes(DefaultMutableTreeNode node, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                node.add(childNode);
                createNodes(childNode, child);
            }
        }
    }

    private class FileTreeMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e) && !fileTree.isSelectionEmpty() && Objects.requireNonNull(fileTree.getSelectionPath()).getPathCount() > 1) {
                showPopupMenu(e);
            }
        }

        private void showPopupMenu(MouseEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            File file = (File) node.getUserObject();

            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(createMenuItem("Download", () -> downloadFile(file)));
            popupMenu.add(createMenuItem("Delete", () -> deleteFile(node, file)));
            popupMenu.show(fileTree, e.getX(), e.getY());
        }

        private JMenuItem createMenuItem(String title, Runnable action) {
            JMenuItem menuItem = new JMenuItem(title);
            menuItem.addActionListener(e -> action.run());
            return menuItem;
        }

        private void downloadFile(File file) {
            User user = UserManager.getUser(username);
            EncryptionService.CipherFile(false, file, new File("src/main/resources/downloads/" + file.getName()), user ,passwordField.getText());
        }

        private void deleteFile(DefaultMutableTreeNode node, File file) {
            if (file.delete()) {
                System.out.println("File deleted successfully");
                DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
                model.removeNodeFromParent(node);
            } else {
                System.out.println("Failed to delete the file");
            }
        }
    }

    private class FileDropTarget extends DropTarget {
        @Override
        public synchronized void drop(DropTargetDropEvent dtde) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            try {
                List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                for (File file : droppedFiles){
                    EncryptionService.CipherFile(true, file, Paths.get("src/main/resources/vault", username, file.getName()).toFile(), UserManager.getUser(username), passwordField.getText());
                }

                // Refresh the file tree
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) fileTree.getModel().getRoot();
                root.removeAllChildren();
                createNodes(root, Paths.get("src/main/resources/vault", username).toFile());
                ((DefaultTreeModel) fileTree.getModel()).reload();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}