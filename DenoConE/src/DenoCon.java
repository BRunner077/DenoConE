import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DenoCon {

    private static String imageFileName = null;
    private static ScrolledComposite imageCanvas = null;
    private static Display display = null;
    private static Image image = null;
    private static Shell shell = null;
    private static ImageData originalImage = null, processedImage = null;
    private static boolean originalDisplayed = true;
    private static Button buttonToggleProcessed = null;
    private static Button buttonProcess = null;
    private static Button buttonMed = null;
    private static int matrixSize = 3;
    private static Text[][] inputMatrix = null;
    private static int[][] convMatrix = null;

    public static void main(String[] args) {

        // Display + shell
        int gridColumns = 5;

        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new GridLayout(gridColumns, false));

        // The scrolled composite
        imageCanvas = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
        GridData layoutData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER, GridData.VERTICAL_ALIGN_CENTER, true, true);
        layoutData.horizontalSpan = gridColumns;
        layoutData.verticalSpan = 1;
        imageCanvas.setLayoutData(layoutData);


        // Input matrix + active Convolution matrix

        inputMatrix = new Text[matrixSize][matrixSize];
        convMatrix = new int[matrixSize][matrixSize];

        // Handle for input verification
        Listener numbersVerifier = new Listener() {
            public void handleEvent(Event e) {
                String string = e.text;
                char[] chars = new char[string.length()];
                string.getChars(0, chars.length, chars, 0);
                for (int i = 0; i < chars.length; i++) {
                    if (!('0' <= chars[i] && chars[i] <= '9' || chars[i] == '-')) {
                        e.doit = false;
                        return;
                    }
                }
            }
        };

        for (int y = 0; y < matrixSize; y++) {
            for (int x = 0; x < matrixSize; x++) {
                Text tmp = new Text(shell, SWT.BORDER | SWT.SINGLE | SWT.RIGHT);
                tmp.setTextLimit(4);
                tmp.setText("0");

                tmp.addListener(SWT.Verify, numbersVerifier);

                layoutData = new GridData(40, 20);
                tmp.setLayoutData(layoutData);

                inputMatrix[y][x] = tmp;
            }
        }

        Label divLabel = new Label(shell, SWT.SINGLE);
        divLabel.setText("Div");
        divLabel.moveAbove(inputMatrix[1][0]);

        final Text divText = new Text(shell, SWT.BORDER | SWT.SINGLE | SWT.RIGHT);
        divText.setTextLimit(4);
        divText.setText("0");

        divText.addListener(SWT.Verify, numbersVerifier);

        layoutData = new GridData(40, 20);
        divText.setLayoutData(layoutData);

        divText.moveAbove(inputMatrix[1][0]);


        Label biasLabel = new Label(shell, SWT.SINGLE);
        biasLabel.setText("Bias");
        biasLabel.moveAbove(inputMatrix[2][0]);

        final Text biasText = new Text(shell, SWT.BORDER | SWT.SINGLE | SWT.RIGHT);
        biasText.setTextLimit(4);
        biasText.setText("0");

        biasText.addListener(SWT.Verify, numbersVerifier);

        layoutData = new GridData(40, 20);
        biasText.setLayoutData(layoutData);

        biasText.moveAbove(inputMatrix[2][0]);

        Label medLabel = new Label(shell, SWT.SINGLE);
        medLabel.setText("Median");
        medLabel.moveBelow(inputMatrix[2][2]);

        final Text medText = new Text(shell, SWT.BORDER | SWT.SINGLE | SWT.RIGHT);
        medText.setTextLimit(4);
        medText.setText("3");

        medText.addListener(SWT.Verify, numbersVerifier);

        layoutData = new GridData(40, 20);
        medText.setLayoutData(layoutData);

        medText.moveBelow(medLabel);


        // Gauss button
        Button buttonGauss = new Button(shell, SWT.PUSH);

        buttonGauss.setText(
                "Gauss Matrix");

        layoutData = new GridData(100, 30);

        buttonGauss.setLayoutData(layoutData);

        // Gauss button handler
        buttonGauss.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {

                int[][] gaussMatrix = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};

                for (int y = 0; y < matrixSize; y++) {
                    for (int x = 0; x < matrixSize; x++) {
                        inputMatrix[y][x].setText(Integer.toString(gaussMatrix[y][x]));
                    }
                }
            }
        });

        // Prewitt button
        Button buttonPrewitt = new Button(shell, SWT.PUSH);
        buttonPrewitt.setText("Prewitt Matrix");

        layoutData = new GridData(100, 30);
        buttonPrewitt.setLayoutData(layoutData);

        // Prewitt button handler
        buttonPrewitt.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {

                int[][] prewittMatrix = {{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};

                for (int y = 0; y < matrixSize; y++) {
                    for (int x = 0; x < matrixSize; x++) {
                        inputMatrix[y][x].setText(Integer.toString(prewittMatrix[y][x]));
                    }
                }
            }
        });


        // Laplace button
        Button buttonLaplace = new Button(shell, SWT.PUSH);
        buttonLaplace.setText("Laplace Matrix");

        layoutData = new GridData(100, 30);
        buttonLaplace.setLayoutData(layoutData);

        // Laplace button handler
        buttonLaplace.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {

                int[][] laplaceMatrix = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

                for (int y = 0; y < matrixSize; y++) {
                    for (int x = 0; x < matrixSize; x++) {
                        inputMatrix[y][x].setText(Integer.toString(laplaceMatrix[y][x]));
                    }
                }
            }
        });
        
                // Process button
        buttonProcess = new Button(shell, SWT.PUSH);
        buttonProcess.setText("Apply matrix");

        layoutData = new GridData(100, 30);
        buttonProcess.setLayoutData(layoutData);
        buttonProcess.setEnabled(false);

        // Process button handler
        buttonProcess.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {

                String tmpString = null;

                for (int y = 0; y < matrixSize; y++) {
                    for (int x = 0; x < matrixSize; x++) {
                        tmpString = inputMatrix[y][x].getText();
                        if (!"".equals(tmpString)) {
                            convMatrix[y][x] = Integer.valueOf(tmpString);
                        } else {
                            convMatrix[y][x] = 0;
                        }
                    }
                }

                tmpString = divText.getText();
                int div = 0;

                if (!"".equals(tmpString)) {
                    div = Integer.valueOf(tmpString);
                }

                tmpString = biasText.getText();
                int bias = 0;

                if (!"".equals(tmpString)) {
                    bias = Integer.valueOf(tmpString);
                }

                processedImage = applyConvMatrix(originalImage, div, bias, convMatrix);

                buttonToggleProcessed.setEnabled(true);
                originalDisplayed = false;
                buttonToggleProcessed.setText("Toggle original");

                drawImage(processedImage);
            }
        });
        
        // Median button
        buttonMed = new Button(shell, SWT.PUSH);
        buttonMed.setText("Apply median");

        layoutData = new GridData(100, 30);
        buttonMed.setLayoutData(layoutData);
        buttonMed.setEnabled(false);

        // Median button handler
        buttonMed.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {

                String tmpString = medText.getText();
                int tmpInt = 3;

                if (!"".equals(tmpString)) {
                    tmpInt = Integer.valueOf(tmpString);
                }
                processedImage = applyMedian(originalImage, tmpInt);

                buttonToggleProcessed.setEnabled(true);
                originalDisplayed = false;
                buttonToggleProcessed.setText("Toggle original");

                drawImage(processedImage);

            }
        });

        // Open image button
        Button buttonOpenFile = new Button(shell, SWT.PUSH);
        buttonOpenFile.setText("Open JPG file");

        layoutData = new GridData(100, 30);
        buttonOpenFile.setLayoutData(layoutData);

        // Open image button handler
        buttonOpenFile.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                dialog.setText("Open a file for processing");
                String[] filterExt = {"*.jpg"};
                dialog.setFilterExtensions(filterExt);
                imageFileName = dialog.open();
                if (imageFileName != null) {
                    DenoCon.loadImage(imageFileName);
                }
            }
        });

        // Toggle image button
        buttonToggleProcessed = new Button(shell, SWT.PUSH);
        buttonToggleProcessed.setText("Toggle processed");
        buttonToggleProcessed.setEnabled(false);

        layoutData = new GridData(100, 30);
        buttonToggleProcessed.setLayoutData(layoutData);

        // Toggle image button handler
        buttonToggleProcessed.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {

                if (originalImage != null && processedImage != null) {
                    if (originalDisplayed) {
                        originalDisplayed = false;
                        buttonToggleProcessed.setText("Toggle original");
                        drawImage(processedImage);
                    } else {
                        originalDisplayed = true;
                        buttonToggleProcessed.setText("Toggle processed");
                        drawImage(originalImage);
                    }
                }
            }
        });

 
        // Run it
        shell.setSize(800, 500);
        shell.setText("Denoise & Convolution");
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
        image.dispose();
    }

    private static void loadImage(String imageFileName) {

        if (imageFileName != null) {
            originalImage = new ImageData(imageFileName);
            processedImage = convertToGrey(originalImage);

            buttonToggleProcessed.setEnabled(true);
            originalDisplayed = true;
            buttonToggleProcessed.setText("Toggle processed");

            buttonProcess.setEnabled(true);
            buttonMed.setEnabled(true);

            drawImage(originalImage);
        }

    }

    private static void drawImage(ImageData imageData) {

        if (imageData != null) {

            if (image != null) {
                image.dispose();
            }
            image = new Image(display, imageData);

            Label imgLabel = new Label(imageCanvas, SWT.NONE);

            imgLabel.setImage(image);
            imgLabel.setSize(imgLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            imageCanvas.setContent(imgLabel);
            shell.layout();
        }
    }

    private static ImageData convertToGrey(ImageData imageData) {
        ImageData tmp = (ImageData) originalImage.clone();

        for (int y = 0; y < tmp.height; y++) {
            for (int x = 0; x < tmp.width; x++) {
                RGBPixel pixel = new RGBPixel(x, y, imageData);
                pixel.convertToGrey();
                pixel.writeToImageData(x, y, tmp);
            }
        }
        return tmp;
    }

    private static ImageData applyMedian(ImageData imageData, int medianSize) {
        ImageData tmp = (ImageData) originalImage.clone();

        PixelArray rgbArray = new PixelArray(imageData);
        rgbArray.median(medianSize);
        rgbArray.writeToImageData(tmp);

        return tmp;
    }

    private static ImageData applyConvMatrix(ImageData imageData, int div, int bias, int[][] matrix) {
        ImageData tmp = (ImageData) originalImage.clone();

        PixelArray rgbArray = new PixelArray(imageData);
        rgbArray.convMatrix(div, bias, matrix);
        rgbArray.writeToImageData(tmp);

        return tmp;
    }
}
