package com.yafieimam.nuhanaapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

import ALI.VectorLib;

public class TestingKlasifikasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_klasifikasi);

        VectorLib vlib = new VectorLib();
        double[] nilai_total = vlib.initArray(54, 0.0);

        DatabaseHandler dbHandler = new DatabaseHandler(TestingKlasifikasiActivity.this);
        double[][] data = dbHandler.getFeatureDataTraining(nilai_total);
        int[] label = dbHandler.getLabelDataTraining();


        data = vlib.Normalization("minmax", data, 0, 1);

        ArrayList<ArrayList<Double>> data_list = new ArrayList<ArrayList<Double>>();

        for(int i = 0; i < data.length; i++){
            ArrayList<Double> list = new ArrayList<>();
            for(int j = 0; j < data[i].length; j++){
                list.add(data[i][j]);
            }
            data_list.add(list);
        }

        Double[] fitur_datates = new Double[data_list.get(data_list.size()-1).size()];
        fitur_datates = data_list.get(data_list.size()-1).toArray(fitur_datates);

//                    Log.d("PercobaanEkstraksi", "Data Fitur 1 : " + Arrays.toString(fitur_datates));

        double[] fitur_data_tes = ArrayUtils.toPrimitive(fitur_datates);
        Log.d("PercobaanEkstraksi", "Data Fitur Tes : " + Arrays.toString(fitur_data_tes));

        data_list.remove(data_list.size()-1);

        double[][] fitur_datatraining = new double[data_list.size()][fitur_data_tes.length];

        for(int i = 0; i < data_list.size(); i++){
            Double[] fitur_temp = new Double[fitur_data_tes.length];
            fitur_temp = data_list.get(i).toArray(fitur_temp);
            double[] temp_fitur = new double[fitur_temp.length];
            temp_fitur = ArrayUtils.toPrimitive(fitur_temp);
            fitur_datatraining[i] = temp_fitur;
        }

        double[][] weightLayerOne = dbHandler.getDataWeightLayerOne();
        double[][] weightLayerTwo = dbHandler.getDataWeightLayerTwo();
        double[][] weightLayerThree = dbHandler.getDataWeightLayerThree();

//                    double[] layerOne = vlib.initArray(weightLayerOne[0].length,0.0);
//                    double[] layerTwo = vlib.initArray(weightLayerTwo[0].length,0.0);
//                    double[] layerThree = vlib.initArray(weightLayerThree[0].length,0.0);
//
//                    for(i = 0; i < weightLayerOne.length; i++){
//                        for(j = 0; j < weightLayerOne[i].length; j++){
//                            if(i == weightLayerOne.length - 1){
//                                layerOne[j] = layerOne[j] + (1 * weightLayerOne[i][j]);
//                            }else {
//                                layerOne[j] = layerOne[j] + (fitur_data_tes[i] * weightLayerOne[i][j]);
//                            }
//                        }
//                    }
//
//                    //Normalisasi Sigmoidal
//                    double total_nilai = 0;
//                    double total_kuadrat = 0;
//                    double exp = 2.718281828;
//                    for(i = 0; i < layerOne.length; i++) {
//                        total_nilai = total_nilai + layerOne[i];
//                        total_kuadrat = total_kuadrat + Math.pow(layerOne[i], 2);
//                    }
//                    double average = total_nilai / layerOne.length;
//                    double total_nilai_kuadrat = Math.pow(total_nilai, 2);
//                    double varian = ((layerOne.length * total_kuadrat) - total_nilai_kuadrat) / (layerOne.length * (layerOne.length - 1));
//                    double standar_deviasi = Math.sqrt(varian);
//                    for(i = 0; i < layerOne.length; i++){
//                        double x = (layerOne[i] - average) / standar_deviasi;
//                        layerOne[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
//                    }
//
//                    Log.d("PercobaanEkstraksi", "LAYER SATU : " + Arrays.toString(layerOne));
//                    Log.d("PercobaanEkstraksi", "JUMLAH LAYER SATU : " + layerOne.length);
//
//                    for(i = 0; i < weightLayerTwo.length; i++){
//                        for(j = 0; j < weightLayerTwo[i].length; j++){
//                            if(i == weightLayerTwo.length - 1){
//                                layerTwo[j] = layerTwo[j] + (1 * weightLayerTwo[i][j]);
//                            }else {
//                                layerTwo[j] = layerTwo[j] + (layerOne[i] * weightLayerTwo[i][j]);
//                            }
//                        }
//                    }
//
//                    //Normalisasi Sigmoidal
//                    total_nilai = 0;
//                    total_kuadrat = 0;
//                    exp = 2.718281828;
//                    for(i = 0; i < layerTwo.length; i++){
//                        total_nilai = total_nilai + layerTwo[i];
//                        total_kuadrat = total_kuadrat + Math.pow(layerTwo[i], 2);
//                    }
//                    average = total_nilai / layerTwo.length;
//                    total_nilai_kuadrat = Math.pow(total_nilai, 2);
//                    varian = ((layerTwo.length * total_kuadrat) - total_nilai_kuadrat) / (layerTwo.length * (layerTwo.length - 1));
//                    standar_deviasi = Math.sqrt(varian);
//                    for(i = 0; i < layerTwo.length; i++){
//                        double x = (layerTwo[i] - average) / standar_deviasi;
//                        layerTwo[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
//                    }
//
//                    Log.d("PercobaanEkstraksi", "LAYER TWO : " + Arrays.toString(layerTwo));
//                    Log.d("PercobaanEkstraksi", "JUMLAH LAYER DUA : " + layerTwo.length);
//
//                    for(i = 0; i < weightLayerThree.length; i++){
//                        for(j = 0; j < weightLayerThree[i].length; j++){
//                            if(i == weightLayerThree.length - 1){
//                                layerThree[j] = layerThree[j] + (1 * weightLayerThree[i][j]);
//                            }else {
//                                layerThree[j] = layerThree[j] + (layerTwo[i] * weightLayerThree[i][j]);
//                            }
//                        }
//                    }
//
//                    //Normalisasi Sigmoidal
//                    total_nilai = 0;
//                    total_kuadrat = 0;
//                    exp = 2.718281828;
//                    for(i = 0; i < layerThree.length; i++){
//                        total_nilai = total_nilai + layerThree[i];
//                        total_kuadrat = total_kuadrat + Math.pow(layerThree[i], 2);
//                    }
//                    average = total_nilai / layerThree.length;
//                    total_nilai_kuadrat = Math.pow(total_nilai, 2);
//                    varian = ((layerThree.length * total_kuadrat) - total_nilai_kuadrat) / (layerThree.length * (layerThree.length - 1));
//                    standar_deviasi = Math.sqrt(varian);
//                    for(i = 0; i < layerThree.length; i++){
//                        double x = (layerThree[i] - average) / standar_deviasi;
//                        layerThree[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
//                    }
//
//                    Log.d("PercobaanEkstraksi", "LAYER OUTPUT : " + Arrays.toString(layerThree));
//                    Log.d("PercobaanEkstraksi", "JUMLAH LAYER TIGA : " + layerThree.length);
//
//                    for(i = 0; i < layerThree.length; i++){
//                        if(layerThree[i] <= 1 || layerThree[i] >= 0.9){
//                            layerThree[i] = 1;
//                        } else if(layerThree[i] <= 0.1 || layerThree[i] >= -0.1){
//                            layerThree[i] = 0;
//                        }
//                    }

//                    Log.d("PercobaanEkstraksi", "HASIL PENGENALAN ADALAH : " + Arrays.toString(layerThree));

        int rows = fitur_datatraining.length;
        int cols = fitur_datatraining[0].length;

        int kelas=20;

        int[][] target = vlib.initArray(rows, kelas, 0);
        int counter = 1;
        int col = 0;
        for (int i = 0; i < rows; i++) {
            target[i][col] = 1;
            counter++;
            if (counter > 27) {
                counter = 1;
                col++;
            }
        }

        int jumlah_sample = 27, jumlah_output = target[0].length;
        boolean ketemu;
        double[] output;
        int z = 0, y = 0;
        int[][] hasil_output = vlib.initArray(jumlah_output, jumlah_sample, -1);
        double dist, err=0, precision;
        for (int k = 0; k < fitur_datatraining.length; k++) {
            output = hitung(weightLayerOne, weightLayerTwo, weightLayerThree, fitur_datatraining[k]);
            dist=vlib.getDistance(output, target[k]);
            if (dist>0) err++;
            ketemu = false;
            int j = 0;
            while ((j < target[0].length) && (!ketemu)) {
                if (output[j] == 1.0) {
                    ketemu = true;
                } else {
                    j++;
                }
            }
            if (ketemu) hasil_output[y][z] = (int) j+1;

            z++;
            if (z == jumlah_sample) {
                z = 0;
                y++;
            }
        }

        Log.d("PercobaanEkstraksi", "HASIL KESELURUHAN : " + Arrays.deepToString(hasil_output));

        err = err * 100 / fitur_datatraining.length;
        precision = vlib.getRound(100 - err, 2);
        Log.d("PercobaanEkstraksi", "PRESISI : " + precision);
    }

    public double[] hitung(double[][] weight1, double[][] weight2, double[][] weight3, double[] datates){
        int bias = 1;
        int[] hidden_unit = {43, 32};
        int jumlah_hidden = hidden_unit.length;
        int jumlah_output = 20;
        double[][] weight_input = new double[datates.length + 1][hidden_unit[0]];
        double[][][] weight_hidden = new double[jumlah_hidden][][];
        for(int a = 0; a < jumlah_hidden - 1; a++){
            weight_hidden[a] = new double[hidden_unit[a] + 1][hidden_unit[(a + 1)]];
        }
        weight_hidden[(jumlah_hidden - 1)] = new double[hidden_unit[(jumlah_hidden - 1)]][jumlah_output];

        //update manual sesuai jumlah hidden layer
        weight_input = weight1;
        weight_hidden[0] = weight2;
        weight_hidden[1] = weight3;

        double[] input_layer = new double[datates.length + 1];
        double[] sigmoid_input = new double[hidden_unit[0]];
        double[][] hidden_layer = new double[jumlah_hidden][];
        for(int a = 0; a < jumlah_hidden; a++){
            hidden_layer[a] = new double[hidden_unit[a] + 1];
        }
        double[][] sigmoid_hidden = new double[jumlah_hidden][];
        for(int a = 0; a < jumlah_hidden - 1; a++){
            sigmoid_hidden[a] = new double[hidden_unit[(a + 1)]];
        }
        sigmoid_hidden[(jumlah_hidden - 1)] = new double[jumlah_output];
        double[] output_layer = new double[jumlah_output];

        input_layer[0] = bias;
        System.arraycopy(datates, 0, input_layer, 1, datates.length);
        VectorLib vlib = new VectorLib();
        double[] hitung_input = hitung_data(input_layer, weight_input);
        for(int a = 0; a < hidden_unit[0]; a++){
            sigmoid_input[a] = (1 / (1 + Math.exp(-1 * hitung_input[a])));
        }
        hidden_layer[0][0] = bias;
        System.arraycopy(sigmoid_input, 0, hidden_layer[0], 1, hidden_unit[0]);
        for(int a = 0; a < jumlah_hidden - 1; a++){
            double[] hitung_hidden = hitung_data(hidden_layer[a], weight_hidden[a]);
            for(int b = 0; b < hidden_unit[(a + 1)]; b++){
                sigmoid_hidden[a][b] = (1 / (1 + Math.exp(-1 * hitung_hidden[b])));
            }
            hidden_layer[(a + 1)][0] = bias;
            System.arraycopy(sigmoid_hidden[a], 0, hidden_layer[(a + 1)], 1, hidden_unit[(a + 1)]);
        }

        double[] hitung_hidden = hitung_data(hidden_layer[(jumlah_hidden - 1)], weight_hidden[(jumlah_hidden - 1)]);
        for(int a = 0; a < jumlah_output; a++){
            sigmoid_hidden[(jumlah_hidden - 1)][a] = (1 / (1 + Math.exp(-1 * hitung_hidden[a])));
        }
        System.arraycopy(sigmoid_hidden[(jumlah_hidden - 1)], 0, output_layer, 0, jumlah_output);
        for(int a = 0; a < jumlah_output; a++){
            if(output_layer[a] > 0.8){
                output_layer[a] = 1;
            }
            if(output_layer[a] < 0.2){
                output_layer[a] = 0;
            }
        }

        return output_layer;
    }

    public double[] hitung_data(double[] data, double[][] weight){
        double[] output = null;
        int colB = weight[0].length;
        int colA = data.length;
        int rowA = 1;
        int rowB = weight.length;
        int count = colA;

        if(colA == rowB){
            output = new double[colB];
            for(int c2 = 0; c2 < colB; c2++){
                int c1 = 0;
                output[c2] = 0;
                while(c1 < count){
                    output[c2] += data[c1] * weight[c1][c2];
                    c1++;
                }
            }
        }

        return output;
    }
}
