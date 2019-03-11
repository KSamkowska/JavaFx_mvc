package controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainWindowController {
	 private Main main;
	 private Stage primaryStage;
	 private GraphicsContext gc;
	 private WritableImage dstImage; //deklaracja zmiennej dla zapisywanego fragmentu obrazka
	 private double redValue; // deklaracja zmiennej ilo≈õci czerwonego koloru dla pojedy≈Ñczego piksela
	 private double averageRedValue; //deklaracja zmiennej dla ≈õredniej ilo≈õci czerwonego koloru fragn=mentu obrazka


		String adres=new String();
		BufferedImage netImage;
	 
	 //deklaracja zmiennych z scenebuilder
		@FXML private Button wczytajButton;
		@FXML private Button czyscButton;
		@FXML private Canvas canvas;
		@FXML private ImageView imageView;
		@FXML private Label labelFoto;
		@FXML private TextField sciezka;
		@FXML private Pane imagePane;
		 
	 //wczytanie kwadratowego kursoru
		 private Image imageCursor = new Image(getClass().getResourceAsStream("Cursor.gif"));
		
	//wykorzystanie TreeMap dla 25 obrazkow posortowanych wg sredniego czerwonego koloru
	 
		private TreeMap<Double, WritableImage> imageSegmentTreeMap = new TreeMap<Double, WritableImage>(
				Collections.reverseOrder());
		private int maxSizeOfimageSegmentTreeMap = 25;
		
		@FXML
		public void onMouseEntered() {//wchodzac na obrazek ma sie pojawic kursor w formie kwadratu
			imageView.setCursor(new ImageCursor(imageCursor));
		}
		
		@FXML
		public void wczytaj() { //onactionbutton metoda wczytujƒÖca obrazek po naci≈õnieciu przycisku Wczytaj
			wczytajObraz();
		}

		@FXML
		public void czysc() { //on actionbutton  metoda czyszczƒÖca obszar 
			if (gc !=null) {
				gc.clearRect(0, 0, 265, 265);
			}
			imageSegmentTreeMap.clear();
		}
		
		
		@FXML
		public void onMousePressed(MouseEvent event) { //nacisniecie kursora powodje kopiowanie obrazka, z wyliczeniem sredniego czerwonego 
			//i posortowaniem wg wartosci czerwonego 
			
				//kopiowanie obrazka do dstImage oraz sumowanie ilosci czerwonego w pikselach
				dstImage = new WritableImage(41, 41);
				PixelReader reader = imageView.getImage().getPixelReader();
				PixelWriter writer = dstImage.getPixelWriter();
				averageRedValue = 0;
				redValue = 0;
				for (int x = 0; x < 41; x++) {
					for (int y = 0; y < 41; y++) {
						
						Color color = reader.getColor((int) ((event.getX()*imageView.getImage().getHeight()/imageView.getFitHeight())+x)
								, (int) ((event.getY()*imageView.getImage().getWidth()/imageView.getFitWidth())+y));
						
						writer.setColor(x, y, color);
						redValue = redValue + color.getRed();
					}
				}
				averageRedValue = redValue / (41*41); //srednia ilosc czerwonego w kwadracie
				imageSegmentTreeMap.put(averageRedValue, dstImage); //wstawienie obrazka do TreeMapy, wg sredniej wartosci czerwonego
				
				System.out.println("úrednia wartoúÊ czerwonego "+ averageRedValue+" pozycja  "+event.getX()+" "+event.getY() );
				
				if (imageSegmentTreeMap.size() > maxSizeOfimageSegmentTreeMap) {
					imageSegmentTreeMap.pollLastEntry();
				}
				//wstawianie obrazkow do canvas po 5 w wierszu
				gc = canvas.getGraphicsContext2D();
				double pozX = 0;
				double pozY = 0;
				int count = 1;

				for (Map.Entry<Double, WritableImage> entry : imageSegmentTreeMap.entrySet()) {
					if (count < 5) {
						gc.drawImage(entry.getValue(), pozX, pozY);
						pozX = pozX + 50; //pozycja + przesuniÍcie o 50 pikseli
						count = count + 1;
					} else {
						gc.drawImage(entry.getValue(), pozX, pozY);
						pozY = pozY + 50;
						pozX = 0;
						count = 1;
					}
				}
			}
		
		
	 @FXML
		public void wczytajObraz() {
			try {	
				
				String adres = sciezka.getText();				
				System.out.println("Scieøka "+adres);
				//utworzenie obiektu URL ze úcieøkπ do obrazu
				URL imageURL = new URL(adres);
				//odczytanie obrazu z adresu URL
				netImage = ImageIO.read(imageURL);
				Image imageNew=new Image(adres);
				ImageView imageView_new=new ImageView(imageNew);
				
				imageView.setImage(imageNew);
				
					
				} catch (IOException ex) {
					labelFoto.setText("Wpisz w≥aúciwy adres URL");
					System.out.println("Wpisz dobry adres");
				}
				};
	 
	 
	 public void setMain(Main main,Stage primaryStage){
		 this.main=main; 
		 this.primaryStage=primaryStage;
		
	 }
	 
}
	