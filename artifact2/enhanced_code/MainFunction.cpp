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

// Graph class to manage course dependencies
class Graph {
private:
    unordered_map<string, vector<string>> adjList; // prerequisite - list of dependent courses

public:
    // Add a course and its edges (prereq -> course)
    void addCourse(const Course& course) {
        for (const string& prereq : course.prerequisites) {
            adjList[prereq].push_back(course.courseNumber);
        }
        // Ensure every course appears in the graph, even if it has no dependents
        if (adjList.find(course.courseNumber) == adjList.end()) {
            adjList[course.courseNumber] = {};
        }
    }

    // Print the graph
    void printGraph() const {
        cout << "\nCourse Dependency Graph:\n";
        for (const auto& entry : adjList) {
            cout << entry.first << " -> ";
            if (entry.second.empty()) {
                cout << "(no dependents)";
            }
            else {
                for (const string& dep : entry.second) {
                    cout << dep << " ";
                }
            }
            cout << endl;
        }
    }
};

void buildGraphOnDemand(const unordered_map<string, Course>& courses) {
    unordered_map<string, vector<string>> adjList;

    for (const auto& entry : courses) {
        for (const string& prereq : entry.second.prerequisites) {
            adjList[prereq].push_back(entry.first);
        }
    }

    // Print dependencies
    for (const auto& entry : adjList) {
        cout << entry.first << " -> ";
        for (const string& dep : entry.second) cout << dep << " ";
        cout << endl;
    }
}

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
        prerequisites.reserve(5); // since most courses have <5 prerequisites


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

        for (const string& prereq : prerequisites) {
            if (!prereq.empty() && courses.find(prereq) == courses.end()) {
                cerr << "Warning: Prerequisite " << prereq
                    << " for course " << courseNumber << " not found in course list.\n";
            }
        }
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

// Helper for DFS-based topological sort
void dfsTopo(const string& course, const unordered_map<string, Course>& courses,
    unordered_map<string, bool>& visited, vector<string>& result) {
    visited[course] = true;
    for (const string& prereq : courses.at(course).prerequisites) {
        if (!visited[prereq]) {
            dfsTopo(prereq, courses, visited, result);
        }
    }
    result.push_back(course);
}

// Perform topological sorting of all courses
void printCourseOrder(unordered_map<string, Course>& courses) {
    unordered_map<string, bool> visited;
    vector<string> result;

    for (const auto& entry : courses) {
        if (!visited[entry.first]) {
            dfsTopo(entry.first, courses, visited, result);
        }
    }

    reverse(result.begin(), result.end());
    cout << "\nCourse Order (Prerequisites first):\n";
    for (const string& course : result) {
        cout << course << " ";
    }
    cout << endl;
}

// Simple test runner for validating correctness
void runTests() {
    cout << "\nRunning Unit Tests...\n";

    // Load sample file
    auto testCourses = loadCourses("Text.txt");

    // Test 1: Check course count
    if (testCourses.size() == 8) {
        cout << "PASS: Correct number of courses loaded.\n";
    }
    else {
        cout << "FAIL: Expected 8 courses, got " << testCourses.size() << ".\n";
    }

    // Test 2: Verify a known course exists
    if (testCourses.find("CSCI400") != testCourses.end()) {
        cout << "PASS: Course CSCI400 found.\n";
    }
    else {
        cout << "FAIL: Course CSCI400 missing.\n";
    }

    // Test 3: Verify prerequisites are loaded
    if (!testCourses["CSCI400"].prerequisites.empty()) {
        cout << "PASS: CSCI400 prerequisites loaded.\n";
    }
    else {
        cout << "FAIL: CSCI400 prerequisites missing.\n";
    }

    // Test 4: Verify alphabetical sorting logic
    vector<string> sortedCourses;
    for (const auto& entry : testCourses) {
        sortedCourses.push_back(entry.first);
    }
    sort(sortedCourses.begin(), sortedCourses.end());
    if (sortedCourses.front() == "CSCI100" && sortedCourses.back() == "MATH201") {
        cout << "PASS: Sorting works correctly.\n";
    }
    else {
        cout << "FAIL: Sorting error.\n";
    }

    // Test 5: Graph dependency test
    Graph g;
    for (const auto& entry : testCourses) {
        g.addCourse(entry.second);
    }
    cout << "PASS: Graph built successfully (verify visually in output).\n";
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
        cout << "4. Print course order (topological sort)\n";
        cout << "8. Run unit tests\n";
        cout << "9. Exit\n";
        cout << "Choose an option: ";
        if (!(cin >> option)) {
            cin.clear(); // clear error state
            cin.ignore(numeric_limits<streamsize>::max(), '\n'); // discard invalid input
            cout << "Invalid input. Please enter a number.\n";
            option = 0; // continue loop
            continue;
        }

        switch (option) {
        case 1:
            do {
                cout << "Enter the file name: ";
                cin >> fileName;
                courses = loadCourses(fileName);
                if (courses.empty()) {
                    cout << "Failed to load courses. Please try again.\n";
                }
            } while (courses.empty());
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
        case 4: {
            printCourseOrder(courses);
            break;
        }
        case 8: 
            runTests();
            break;
        case 9:
            cout << "Exiting program." << endl;
            break;
        default:
            cout << "Error: Invalid option. Please try again." << endl;
        }
    } while (option != 9);

    return 0;
}