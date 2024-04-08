import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Main extends JPanel
{

    private double[] numbers;
    private double[] originalNumbers;
    private int[] originalIndices;
    private boolean[] sorted;
    private boolean isSorted;
    private int[] rectXPositions;
    private int[] textXPositions;
    private int[] textYPositions;
    private JComboBox<String> sortComboBox;
    private JButton sortButton;
    private JButton resetButton;
    private int stepDelay = 500;
    private Timer timer;

    public Main(double[] numbers)
    {
        this.numbers = numbers;
        this.originalNumbers = numbers.clone();
        this.originalIndices = new int[numbers.length];
        this.sorted = new boolean[numbers.length];
        this.rectXPositions = new int[numbers.length];
        this.textXPositions = new int[numbers.length];
        this.textYPositions = new int[numbers.length];
        this.sortComboBox = new JComboBox<>(new String[]{"Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort", "Heap Sort"});
        this.sortButton = new JButton("Sort");
        this.resetButton = new JButton("Reset");
        this.sortButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String selectedSort = (String) sortComboBox.getSelectedItem();
                resetArray();
                switch (selectedSort)
                {
                    case "Bubble Sort":
                        bubbleSort();
                        break;
                    case "Insertion Sort":
                        insertionSort();
                        break;
                    case "Selection Sort":
                        selectionSort();
                        break;
                    case "Merge Sort":
                        mergeSort();
                        break;
                    case "Quick Sort":
                        quickSort();
                        break;
                    case "Heap Sort":
                        heapSort();
                        break;
                }
            }
        });

        this.resetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                resetArray();
                repaint();
            }
        });

        add(sortComboBox);
        add(sortButton);
        add(resetButton);

        for (int i = 0; i < numbers.length; i++)
        {
            originalIndices[i] = i;
            sorted[i] = false;
            rectXPositions[i] = 20 + i * 60;
            textXPositions[i] = rectXPositions[i] + 25;
            textYPositions[i] = getHeight() - 5;
        }
        setPreferredSize(new Dimension(800, 400));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int rectWidth = 50;
        int maxHeight = 200;

        for (int i = 0; i < numbers.length; i++)
        {
            int rectHeight = (int) (numbers[i] * 10);
            if (rectHeight > maxHeight)
            {
                rectHeight = maxHeight;
            }

            if (sorted[i])
            {
                g.setColor(Color.GREEN);
            } else
            {
                g.setColor(Color.RED);
            }

            g.fillRect(rectXPositions[i], getHeight() - rectHeight, rectWidth, rectHeight);

            g.setColor(Color.BLACK);
            g.drawRect(rectXPositions[i], getHeight() - rectHeight, rectWidth, rectHeight);

            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            String numberString = Integer.toString((int) numbers[i]);
            int textWidth = fm.stringWidth(numberString);
            int textHeight = fm.getHeight();
            g.drawString(numberString, textXPositions[i] - textWidth / 2, textYPositions[i] - (textHeight - rectHeight) / 2);
        }
    }

    public void updateVisualization()
    {
        for (int i = 0; i < numbers.length; i++)
        {
            textYPositions[i] = getHeight() - 5 - (int) (numbers[i] * 10);
        }
        repaint();
    }

    public void resetArray()
    {
        numbers = originalNumbers.clone();
        for (int i = 0; i < numbers.length; i++)
        {
            sorted[i] = false;
        }
        isSorted = false;
    }

    public void bubbleSort()
    {
        timer = new Timer(stepDelay, new ActionListener()
        {
            int i = 0;
            int j = 0;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!isSorted)
                {
                    if (i < numbers.length - 1)
                    {
                        if (j < numbers.length - i - 1)
                        {
                            if (numbers[j] > numbers[j + 1])
                            {
                                double temp = numbers[j];
                                numbers[j] = numbers[j + 1];
                                numbers[j + 1] = temp;

                                int tempIndex = originalIndices[j];
                                originalIndices[j] = originalIndices[j + 1];
                                originalIndices[j + 1] = tempIndex;

                                updateVisualization();
                            }
                            j++;
                        }
                        else
                        {
                            sorted[numbers.length - i - 1] = true;
                            j = 0;
                            i++;
                        }
                    }
                    else
                    {
                        sorted[0] = true;
                        isSorted = true;
                        timer.stop();
                    }
                }
            }
        });
        timer.start();
    }

    public void insertionSort()
    {
        timer = new Timer(stepDelay, new ActionListener()
        {
            int i = 1;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!isSorted)
                {
                    if (i < numbers.length)
                    {
                        double key = numbers[i];
                        int j = i - 1;
                        while (j >= 0 && numbers[j] > key)
                        {
                            numbers[j + 1] = numbers[j];
                            originalIndices[j + 1] = originalIndices[j];
                            j = j - 1;
                            updateVisualization();
                        }
                        numbers[j + 1] = key;
                        sorted[i] = true;
                        i++;
                    }
                    else
                    {
                        sorted[numbers.length - 1] = true;
                        isSorted = true;
                        timer.stop();
                    }
                }
            }
        });
        timer.start();
    }

    public void selectionSort()
    {
        timer = new Timer(stepDelay, new ActionListener()
        {
            int i = 0;
            int minIndex;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!isSorted)
                {
                    if (i < numbers.length - 1)
                    {
                        minIndex = i;
                        for (int j = i + 1; j < numbers.length; j++)
                        {
                            if (numbers[j] < numbers[minIndex])
                            {
                                minIndex = j;
                            }
                        }
                        double temp = numbers[minIndex];
                        numbers[minIndex] = numbers[i];
                        numbers[i] = temp;

                        int tempIndex = originalIndices[minIndex];
                        originalIndices[minIndex] = originalIndices[i];
                        originalIndices[i] = tempIndex;

                        sorted[i] = true;
                        i++;
                        updateVisualization();
                    }
                    else
                    {
                        sorted[numbers.length - 1] = true;
                        isSorted = true;
                        timer.stop();
                    }
                }
            }
        });
        timer.start();
    }

    public void mergeSort()
    {
        timer = new Timer(stepDelay, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mergeSortHelper(0, numbers.length - 1);
                isSorted = true;
                timer.stop();
            }
        });
        timer.start();
    }

    private void mergeSortHelper(int low, int high)
    {
        if (!isSorted)
        {
            if (low < high)
            {
                int mid = (low + high) / 2;
                mergeSortHelper(low, mid);
                mergeSortHelper(mid + 1, high);
                merge(low, mid, high);
                updateVisualization();
            }
        }
    }

    private void merge(int low, int mid, int high)
    {
        double[] tempArray = new double[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= high)
        {
            if (numbers[i] <= numbers[j])
            {
                tempArray[k] = numbers[i];
                i++;
            }
            else
            {
                tempArray[k] = numbers[j];
                j++;
            }
            k++;
        }

        while (i <= mid)
        {
            tempArray[k] = numbers[i];
            i++;
            k++;
        }

        while (j <= high)
        {
            tempArray[k] = numbers[j];
            j++;
            k++;
        }

        for (i = 0; i < tempArray.length; i++)
        {
            numbers[low + i] = tempArray[i];
        }
    }

    public void quickSort()
    {
        timer = new Timer(stepDelay, new ActionListener()
        {
            int[] stack = new int[numbers.length];
            int top = -1;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!isSorted)
                {
                    if (top == -1)
                    {
                        stack[++top] = 0;
                        stack[++top] = numbers.length - 1;
                    }
                    if (top >= 0)
                    {
                        int high = stack[top--];
                        int low = stack[top--];
                        int pi = partition(low, high);
                        if (low < pi - 1)
                        {
                            stack[++top] = low;
                            stack[++top] = pi - 1;
                        }
                        if (pi + 1 < high)
                        {
                            stack[++top] = pi + 1;
                            stack[++top] = high;
                        }
                        updateVisualization();
                    }
                    else
                    {
                        isSorted = true;
                        timer.stop();
                    }
                }
            }
        });
        timer.start();
    }

    private int partition(int low, int high)
    {
        double pivot = numbers[high];
        int i = low - 1;
        for (int j = low; j < high; j++)
        {
            if (numbers[j] < pivot)
            {
                i++;
                double temp = numbers[i];
                numbers[i] = numbers[j];
                numbers[j] = temp;

                int tempIndex = originalIndices[i];
                originalIndices[i] = originalIndices[j];
                originalIndices[j] = tempIndex;
            }
        }
        double temp = numbers[i + 1];
        numbers[i + 1] = numbers[high];
        numbers[high] = temp;

        int tempIndex = originalIndices[i + 1];
        originalIndices[i + 1] = originalIndices[high];
        originalIndices[high] = tempIndex;

        return i + 1;
    }

    public void heapSort()
    {
        timer = new Timer(stepDelay, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buildMaxHeap();
                for (int i = numbers.length - 1; i > 0; i--)
                {
                    double temp = numbers[0];
                    numbers[0] = numbers[i];
                    numbers[i] = temp;

                    int tempIndex = originalIndices[0];
                    originalIndices[0] = originalIndices[i];
                    originalIndices[i] = tempIndex;

                    maxHeapify(0, i);
                    updateVisualization();
                }
                sorted[0] = true;
                isSorted = true;
                timer.stop();
            }
        });
        timer.start();
    }

    private void buildMaxHeap()
    {
        for (int i = numbers.length / 2 - 1; i >= 0; i--)
        {
            maxHeapify(i, numbers.length);
        }
    }

    private void maxHeapify(int i, int heapSize)
    {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;
        if (left < heapSize && numbers[left] > numbers[largest])
        {
            largest = left;
        }
        if (right < heapSize && numbers[right] > numbers[largest])
        {
            largest = right;
        }
        if (largest != i)
        {
            double temp = numbers[i];
            numbers[i] = numbers[largest];
            numbers[largest] = temp;

            int tempIndex = originalIndices[i];
            originalIndices[i] = originalIndices[largest];
            originalIndices[largest] = tempIndex;

            maxHeapify(largest, heapSize);
        }
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Chisla molq ");
        String input = scanner.nextLine();

        if (input.isEmpty())
        {
            System.out.println(":(");
            return;
        }

        String[] numberStrings = input.split(" ");
        double[] numbers = new double[numberStrings.length];
        try
        {
            for (int i = 0; i < numberStrings.length; i++)
            {
                numbers[i] = Double.parseDouble(numberStrings[i].trim());
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Ti si go sortirai tva");
            return;
        }

        JFrame frame = new JFrame("Array Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main visualizer = new Main(numbers);
        frame.add(visualizer, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        controlPanel.add(visualizer.sortComboBox);
        controlPanel.add(visualizer.sortButton);
        controlPanel.add(visualizer.resetButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        scanner.close();
    }
}
