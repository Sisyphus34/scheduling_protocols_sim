/*
 * Name: Shawn Picardy
 * Course: CPSC 3220
 * Program 3
 * March 24
 */

// arrival time and service time input file. example: 3 4 from instruction page.
#include <iostream>
#include <stdlib.h>
#include <vector>
#include <string>
using namespace std;

vector<int> arrivalTime;
vector<int> serviceTime;

void printScheduling(string a)
{
    // additional string versions per the examples
    string rrSuffix = "(time slice is 1)";
    string sjfSuffix = "(preemptive)";

    //Convert incoming string to uppercase
    transform(a.begin(), a.end(), a.begin(), ::toupper);

    //Print appropriate heading per string parameter
    if (a == "FIFO")
    {
        cout << "\n" + a + " scheduling results" << endl;
    }
    else if(a == "SJF"){
        cout << "\n" + a + sjfSuffix + " scheduling results" << endl;
    }
    else if (a == "RR"){
        cout << "\n" + a + " scheduling results " + rrSuffix << endl;
    }
    else
    {
        cout << "AN ERRROR OCCURED" << endl;
    }
    
}

int main(int argc, char const *argv[])
{

    printScheduling(argv[1]);
    return 0;
}
