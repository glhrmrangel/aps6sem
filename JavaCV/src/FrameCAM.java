package webcam;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.BytePointer;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imencode;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

/**
 * @author Lucas-HMSC
 */
public class FrameCAM extends javax.swing.JFrame {
    
    private FrameCAM.DaemonThread myThread = null;
    
    //JavaCV
    VideoCapture webSource = null;
    Mat cameraImage = new Mat();
    CascadeClassifier cascade = new CascadeClassifier("/home/lucas/aps6sem/photos/haarcascade_frontalface_alt2.xml");
    BytePointer mem = new BytePointer();
    RectVector detectedFaces = new RectVector();
    
    //Vars
    String root;
    int numSamples = 25, sample = 1, id = 1;
    
    public FrameCAM() {
        initComponents();
        startCamera();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label_photo = new javax.swing.JLabel();
        counterLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        optIniciar = new javax.swing.JMenuItem();
        optSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(label_photo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 250));

        counterLabel.setText("00");
        counterLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(counterLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 50, 30));

        jLabel1.setText("Capture");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, -1, -1));

        jMenu1.setText("File");

        optIniciar.setText("Iniciar");
        jMenu1.add(optIniciar);

        optSair.setText("Sair");
        optSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optSairActionPerformed(evt);
            }
        });
        jMenu1.add(optSair);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void optSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_optSairActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameCAM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel counterLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel label_photo;
    private javax.swing.JMenuItem optIniciar;
    private javax.swing.JMenuItem optSair;
    // End of variables declaration//GEN-END:variables

    class DaemonThread implements Runnable {
        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    try {
                        if (webSource.grab()) {
                            webSource.retrieve(cameraImage);
                            Graphics g; //mostra a imagem no jlabel
                            g = label_photo.getGraphics();

                            Mat imageColor = new Mat(); //imagem colorida
                            imageColor = cameraImage;

                            Mat imageGray = new Mat(); //imagem pb
                            cvtColor(imageColor, imageGray, COLOR_BGRA2GRAY);
//                            flip(cameraImage, cameraImage, +1);

                            RectVector detectedFaces = new RectVector(); //face detectada
                            cascade.detectMultiScale(imageColor, detectedFaces, 1.1, 1, 1, new Size(150, 150), new Size(500, 500));

                            for (int i = 0; i < detectedFaces.size(); i++) { //repetição pra encontrar as faces
                                Rect dadosFace = detectedFaces.get(0);

                                rectangle(imageColor, dadosFace, new Scalar(255, 255, 0, 2), 3, 0, 0);

                                Mat face = new Mat(imageGray, dadosFace);
                                opencv_imgproc.resize(face, face, new Size(160, 160));

                                if (sample <= numSamples) {
                                    //salva a imagem cortada [160,160]
                                    //nome do arquivo: idpessoa + a contagem de fotos. ex: person.10(id).6(sexta foto).jpg
                                    String cropped = "/home/lucas/aps6sem/photos/person." + id++ + "." + sample + ".jpg";
                                    imwrite(cropped, face);

                                    //System.out.println("Foto " + amostra + " capturada\n");
                                    counterLabel.setText(String.valueOf(sample) + "/25");
                                    sample++;
                                }
                                if (sample > 25) {
                                    new TrainLBPH().trainPhotos();//se a contagem for maior que 25, termina de tirar a foto, gera o arquivo
                                    System.out.println("File Generated");
                                    stopCamera(); // e fecha a camera
                                }

                            }
                                
                        }

                        imencode(".bmp", cameraImage, mem);
                        Image im = ImageIO.read(new ByteArrayInputStream(mem.getStringBytes()));
                        BufferedImage buff = (BufferedImage) im;
                        try {
                            if (g.drawImage(buff, 0, 0, 360, 390, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                if (runnable == false) {
                                    System.out.println("Salve a Foto");
                                    this.wait();
                                }
                            }
                        } catch (Exception e) {
                            }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void stopCamera(){
        myThread.runnable = false;
        webSource.release();
        dispose();
    }
    public void startCamera(){
        /*new Thread() {
            @Override
            public void run() {
                webSource = new VideoCapture(0);

                myThread = new FrameCAM.DaemonThread();
                Thread t = new Thread(myThread);
                t.setDaemon(true);
                myThread.runnable = true;
                t.start();
            }
        }.start();*/
        webSource = new VideoCapture(0);
        myThread = new FrameCAM.DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
    }
}
