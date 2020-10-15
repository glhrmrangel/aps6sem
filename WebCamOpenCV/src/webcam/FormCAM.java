package webcam;

import java.awt.Dimension;

/**
 * @author Lucas-HMSC
 */
public class FormCAM extends javax.swing.JFrame {

    public FormCAM() {
        initComponents();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        optIniciar = new javax.swing.JMenuItem();
        optSair = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jMenu1.setText("File");

        optIniciar.setText("Iniciar");
        optIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optIniciarActionPerformed(evt);
            }
        });
        jMenu1.add(optIniciar);

        optSair.setSelected(true);
        optSair.setText("Sair");
        optSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optSairActionPerformed(evt);
            }
        });
        jMenu1.add(optSair);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void optSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_optSairActionPerformed

    private void optIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optIniciarActionPerformed
        PainelCAM painel = new PainelCAM();
        painel.setVisible(true);
        painel.setSize(new Dimension(600, 480));
        this.add(painel);
    }//GEN-LAST:event_optIniciarActionPerformed

    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormCAM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem optIniciar;
    private javax.swing.JCheckBoxMenuItem optSair;
    // End of variables declaration//GEN-END:variables
}
