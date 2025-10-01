#include <iostream>
#include <fstream>
#include <sstream>
#include <unordered_map>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

// Define the Course structure
struct Course {
    string courseNumber;
    string courseName;
    vector<string> prerequisites;

    // Default constructor
    Course() : courseNumber(""), courseName(""), prerequisites({}) {}

    // Constructor for initializing a Course object
    Course(string number, string name, vector<string> prereqs)
        : courseNumber(number), courseName(name), prerequisites(prereqs) {}
};

// Function to load courses from a file into a hash table
unordered_map<string, Course> loadCourses(const string& fileName) {
    unordered_map<string, Course> courses;
    ifstream file(fileName);

    if (!file.is_open()) {
        cerr << "Error: Could not open file " << fileName << endl;
        return courses;
    }

    string line;
    while (getline(file, line)) {
        stringstream ss(line);
        string courseNumber, courseName, prereq;
        vector<string> prerequisites;

        // Read course number and name
        getline(ss, courseNumber, '\t');
        getline(ss, courseName, '\t');

        // Read prerequisites (if any)
        while (getline(ss, prereq, '\t')) {
            if (!prereq.empty()) {
                prerequisites.push_back(prereq);
            }
        }

        // Create a Course object and add it to the hash table
        courses[courseNumber] = Course(courseNumber, courseName, prerequisites);
    }

    file.close();
    return courses;
}

// Function to print all courses in alphanumeric order
void printCourses(const unordered_map<string, Course>& courses) {
    vector<string> courseNumbers;

    // Collect course numbers
    for (const auto& entry : courses) {
        courseNumbers.push_back(entry.first);
    }

    // Sort course numbers
    sort(courseNumbers.begin(), courseNumbers.end());

    // Print sorted course information
    for (const string& number : courseNumbers) {
        const Course& course = courses.at(number);
        cout << course.courseNumber << ": " << course.courseName << endl;
    }
}

// Function to print course information and prerequisites
void printCourseInfo(const unordered_map<string, Course>& courses, const string& courseNumber) {
    auto it = courses.find(courseNumber);
    if (it != courses.end()) {
        const Course& course = it->second;
        cout << "Course Number: " << course.courseNumber << endl;
        cout << "Course Name: " << course.courseName << endl;
        if (!course.prerequisites.empty()) {
            cout << "Prerequisites: ";
            for (const string& prereq : course.prerequisites) {
                cout << prereq << " ";
            }
            cout << endl;
        }
        else {
            cout << "No prerequisites." << endl;
        }
    }
    else {
        cout << "Course not found." << endl;
    }
}

// Main function to run the program
int main() {
    unordered_map<string, Course> courses;
    int option;
    string fileName;

    do {
        cout << "\nMenu:\n";
        cout << "1. Load course data from file\n";
        cout << "2. Print all courses\n";
        cout << "3. Print course information\n";
        cout << "9. Exit\n";
        cout << "Choose an option: ";
        cin >> option;

        switch (option) {
        case 1:
            cout << "Enter the file name: ";
            cin >> fileName;
            courses = loadCourses(fileName);
            break;
        case 2:
            printCourses(courses);
            break;
        case 3: {
            string courseNumber;
            cout << "Enter the course number: ";
            cin >> courseNumber;
            printCourseInfo(courses, courseNumber);
            break;
        }
        case 9:
            cout << "Exiting program." << endl;
            break;
        default:
            cout << "Error: Invalid option. Please try again." << endl;
        }
    } while (option != 9);

    return 0;
}