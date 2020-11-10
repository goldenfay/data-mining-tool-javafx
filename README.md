# Data Mining Package 
<!-- ![introduction](assets/images/intro.gif) -->
<img  src="./assets/images/intro.gif" />

## Overview

<!-- ![overview info](assets/images/main_window.PNG?raw=true) -->
<img  src="./assets/images/main_window.PNG?raw=true" />
A Java tool for applying Data Mining algorithms, implemented in JavaFx.


## Project organisation

The project is basically devided into two main parts:
 1. The **preprocessing** section :  This part of the application is dedicated for:
 	- **Data exploration** : The application allows user to visualize the Dataset in a tabular form, with ability to analyse any column (Information about column data type, range, missing values, etc..). Besides getting an summary of the whole Dataset.
 	- **Correlation analysis** : The application offers the possibility tocalculate correlation coifficient between two column, and plot correlation plot in order to better analyse the Dataset.
 	- **Plots** : For best user experience, the application provides a plenty of plots. For each column, a ***line*** and ***scatter*** plots are displayed to understand data distribution, besides ***boxplots*** and ***outlier analysis***. ***Qplots and QQplots*** are also provided to understand distributions and relations between columns.
 	- **Data normalization** : It is possible to normalize certain column throught the application,  in order to be able to perform mining algorithms 
 2. **Patterns and clusters extraction** section :  In this part, user can choose any of the available algorithms and then visualize the extracted patterns or clusters repartition.


## Implemented algorithms
- Association rules and frequent patterns  extraction algorithms: 
	- Apriori
	- Eclat
	- Fp-Growth
- Clustering algorithms:
	- K-means
	- K-medoids
	- CLARANS
	- DBScan (Beta)
	

## Features

 ### Upload a .arrf dataset file


 <!-- ![upload info](assets/images/choose_dataset.PNG?raw=true) -->
 
<img  src="./assets/images/choose_dataset.PNG?raw=true" />

 ### Dataset Visualization


 <!-- ![visualization1 info](assets/images/visualization_1.PNG?raw=true) -->
 <!-- ![visualization2 info](assets/images/visualization_2.PNG?raw=true) -->
 <!-- ![visualization3 info](assets/images/visualization_3.PNG?raw=true) -->
 <!-- ![visualization4 info](assets/images/visualization_4.PNG?raw=true) -->

<img  src="./assets/images/visualization_1.PNG?raw=true" />
<img  src="./assets/images/visualization_2.PNG?raw=true" />
<img  src="./assets/images/visualization_3.PNG?raw=true" />
<img  src="./assets/images/visualization_4.PNG?raw=true" />


 ### Dataset Preprocessing


 <!-- ![preprocess info](assets/images/preprocessing.PNG?raw=true) -->
<img  src="./assets/images/preprocessing.PNG?raw=true" />

 ### Frequent patterns extraction


 <!-- ![image](assets/images/apriori.PNG?raw=true) -->
<img  src="./assets/images/apriori.PNG?raw=true" />

 
 ### Clustering


 <!-- ![image](assets/images/clustering.PNG?raw=true) -->
<img  src="./assets/images/clustering.PNG?raw=true" />

## Credit

All rights go to [goldenfay](https://github.com/goldenfay) . If you want to use this tool for academic peurposes, please contact me.

## Contribution

Any contribution is more then welcome. If you have any suggestion, please open an issue.