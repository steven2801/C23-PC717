import { AxiosResponse } from "axios";

export const getLabelsPercentagesAndCategory = (response: AxiosResponse) => {
  const { labels, results, max } = JSON.parse(response.data) as {
    labels: string[];
    results: number[];
    max: [string, number];
  };

  // const bestResultIndex = results.indexOf(Math.max(...results));
  // const category = labels[bestResultIndex].split(" ")[0];

  const category = max[0].split("_")[0];

  const percentages: string[] = results.map((value) => `${(value * 100).toFixed(1)}%`);

  // Create an array of objects containing label and percentage
  // const data = labels.map((label, index) => ({ label, percentage: percentages[index] }));

  // Sort the data array in descending order based on percentage
  // data.sort((a, b) => {
  //   const percentageA = parseFloat(a.percentage);
  //   const percentageB = parseFloat(b.percentage);
  //   return percentageB - percentageA;
  // });

  // Update labels and percentages arrays based on the sorted data
  // for (let i = 0; i < data.length; i++) {
  //   labels[i] = data[i].label;
  //   percentages[i] = data[i].percentage;
  // }

  return { labels, percentages, category };
};
