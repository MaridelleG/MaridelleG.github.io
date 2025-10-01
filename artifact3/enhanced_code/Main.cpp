#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include <sstream>  // Needed for stringstream
using namespace std;

// Base class Pet
// Abstraction: Pet represents the concept of any pet.
// Encapsulation: Protected members hide implementation details. 
class Pet {
protected:
    string name;
    int age;
    int spaceNumber;
    int daysStay; // Represents length of stay

public:
    Pet(string n, int a, int space, int stay)
        : name(n), age(a), spaceNumber(space), daysStay(stay) {}

    virtual void displayInfo() = 0;   // Polymorphism: display info differently for Dog/Cat
    virtual string toFileString() = 0; // Converts pet info to a string for file storage
    virtual void setDaysStay(int stay) { daysStay = stay;  } // Setter for daysStay

    // Getter methods for accessing protected members
    string getName() { return name; }
    int getSpaceNumber() { return spaceNumber; }
    int getDaysStay() { return daysStay; }
};

// Forward declarations for file functions
void savePetToFile(Pet* pet);
void updateFileAfterCheckout();
void updatePetData();

// Derived Dog class
// Inheritance: Dog inherits from pet and adds specific attributes
class Dog : public Pet {
public:
    double weight;
    bool grooming;

public:
    Dog(string n, int a, int space, int stay, double w, bool g)
        : Pet(n, a, space, stay), weight(w), grooming(g) {}

    // Convert Dog info to a CSV string for file storage
    string toFileString() override {
        return "Dog," + name + "," + to_string(age) + "," + to_string(spaceNumber) + "," +
            to_string(daysStay) + "," + to_string(weight) + "," + (grooming ? "Yes" : "No");
    }

    void displayInfo() override {
        cout << "Dog Check-in Successful!" << endl;
        cout << "Name: " << name << " | Age: " << age
            << " | Space: " << spaceNumber << " | Stay: " << daysStay << " days"
            << " | Weight: " << weight
            << " | Grooming: " << (grooming ? "Yes" : "No") << endl;
    }
};

// Derived Cat class
// Inheritance: Cat inherits from pet
class Cat : public Pet {
public:
    Cat(string n, int a, int space, int stay)
        : Pet(n, a, space, stay) {}

    string toFileString() override {
        return "Cat," + name + "," + to_string(age) + "," + to_string(spaceNumber) + "," + to_string(daysStay);
    }

    void displayInfo() override {
        cout << "Cat Check-in Successful!" << endl;
        cout << "Name: " << name << " | Age: " << age
            << " | Space: " << spaceNumber << " | Stay: " << daysStay << " days" << endl;
    }
};

// Global storage
// Pets vector acts as in-memory database table
vector<Pet*> pets;
int dogSpaces = 30;  // Available dog spaces
int catSpaces = 12;  // Available cat spaces

// Save pet to file
void savePetToFile(Pet* pet) {
    ofstream outFile("pets.txt", ios::app); // Append new pet
    if (!outFile) {
        cout << "Error opening pets.txt for writing." << endl;
        return;
    }
    outFile << pet->toFileString() << endl;
    outFile.close();
}

// Update file after checkout
void updateFileAfterCheckout() {
    ofstream outFile("pets.txt", ios::trunc); // Overwrite the file
    if (!outFile) {
        cout << "Error updating pets.txt." << endl;
        return;
    }

    // Write all remaining pets to file
    for (Pet* p : pets) {
        outFile << p->toFileString() << endl;
    }
    outFile.close();
}

// Load pets from file at startup
void loadPetsFromFile() {
    ifstream inFile("pets.txt");
    if (!inFile) return;  // If file doesn't exist, skip

    string line;
    while (getline(inFile, line)) {
        string type, name, groomingStr;
        int age, space, stay;
        double weight = 0.0;
        bool grooming = false;

        stringstream ss(line);
        getline(ss, type, ',');   // Dog or Cat
        getline(ss, name, ',');   // Name
        ss >> age; ss.ignore();
        ss >> space; ss.ignore();
        ss >> stay; ss.ignore();

        if (type == "Dog") {
            ss >> weight; ss.ignore();
            getline(ss, groomingStr);
            grooming = (groomingStr == "Yes");
            Dog* dog = new Dog(name, age, space, stay, weight, grooming);
            pets.push_back(dog);
            dogSpaces--;
        }
        else if (type == "Cat") {
            Cat* cat = new Cat(name, age, space, stay);
            pets.push_back(cat);
            catSpaces--;
        }
    }

    inFile.close();
}

// Function to display all pets already checked in
void viewAllPets() {
    if (pets.empty()) {
        cout << "No pets are currently boarded." << endl;
        return;
    }

    cout << "\n--- List of Boarded Pets ---" << endl;
    for (Pet* p : pets) {
        p->displayInfo();  // Use polymorphism to display Dog or Cat info
        cout << "--------------------------" << endl;
    }
}

// Function to search for a pet by its name
void searchPetByName() {
    string searchName;
    cout << "Enter pet name to search: ";
    cin >> searchName;

    bool found = false;
    for (Pet* p : pets) {
        if (p->getName() == searchName) {
            cout << "Pet found!" << endl;
            p->displayInfo();
            found = true;
            break;
        }
    }

    if (!found) {
        cout << "No pet found with the name: " << searchName << endl;
    }
}

//Function to update pet information
void updatePetData() {
    string name;
    cout << "Enter the name of the pet to update: ";
    cin >> name;

    for (Pet* p : pets) {
        if (p->getName() == name) {
            cout << "Pet found. What do you want to update?" << endl;
            cout << "1. Length of stay" << endl;
            if (Dog* d = dynamic_cast<Dog*>(p)) {
                cout << "2. Grooming option" << endl;
            }
            int choice;
            cin >> choice;

            switch (choice) {
            case 1: {
                int newStay;
                cout << "Enter new length of stay: ";
                cin >> newStay;
                p->setDaysStay(newStay);
                cout << "Length of stay updated." << endl;
                break;
            }
            case 2: {
                if (Dog* d = dynamic_cast<Dog*>(p)) {
                    char groomChoice;
                    cout << "Does the owner want grooming? (y/n): ";
                    cin >> groomChoice;
                    d->grooming = (groomChoice == 'y' || groomChoice == 'Y');
                    cout << "Grooming option updated." << endl;
                }
                else {
                    cout << "Invalid choice for a cat." << endl;
                }
                break;
            }
            default:
                cout << "Invalid choice." << endl;
            }
            updateFileAfterCheckout(); // Save updated info to file
            return;
        }
    }
    cout << "No pet found with the name: " << name << endl;
}

// Check-in function
void checkInPet() {
    string petType;
    cout << "Is the pet a dog or a cat? ";
    cin >> petType;

    if (petType == "dog") {
        if (dogSpaces > 0) {
            string name;
            int age, stay;
            double weight;
            char groomChoice;

            cout << "Enter pet name: ";
            cin >> name;
            cout << "Enter pet age: ";
            cin >> age;
            cout << "Enter pet weight: ";
            cin >> weight;
            cout << "Enter length of stay (days): ";
            cin >> stay;

            bool grooming = false;
            if (stay >= 2) {
                cout << "Does the owner want grooming? (y/n): ";
                cin >> groomChoice;
                grooming = (groomChoice == 'y' || groomChoice == 'Y');
            }

            Dog* dog = new Dog(name, age, (31 - dogSpaces), stay, weight, grooming);
            pets.push_back(dog);
            dogSpaces--;
            dog->displayInfo();
            savePetToFile(dog);
        }
        else {
            cout << "Sorry, no dog boarding space available." << endl;
        }
    }
    else if (petType == "cat") {
        if (catSpaces > 0) {
            string name;
            int age, stay;

            cout << "Enter pet name: ";
            cin >> name;
            cout << "Enter pet age: ";
            cin >> age;
            cout << "Enter length of stay (days): ";
            cin >> stay;

            Cat* cat = new Cat(name, age, (13 - catSpaces), stay);
            pets.push_back(cat);
            catSpaces--;
            cat->displayInfo();
            savePetToFile(cat);
        }
        else {
            cout << "Sorry, no cat boarding space available." << endl;
        }
    }
    else {
        cout << "Invalid pet type. Please choose 'dog' or 'cat'." << endl;
    }
}

// Check-out function
void checkOutPet() {
    string name;
    cout << "Enter the name of the pet to check out: ";
    cin >> name;

    bool found = false;

    for (auto it = pets.begin(); it != pets.end(); ++it) {
        if ((*it)->getName() == name) {
            cout << name << " has been checked out from space " << (*it)->getSpaceNumber() << "." << endl;

            // Free up space
            if (dynamic_cast<Dog*>(*it)) dogSpaces++;
            else if (dynamic_cast<Cat*>(*it)) catSpaces++;

            delete* it;          // Free memory
            pets.erase(it);      // Remove from vector
            updateFileAfterCheckout(); // Update file to reflect checkout
            found = true;
            break;
        }
    }

    if (!found) {
        cout << "No pet found with the name: " << name << endl;
    }
}

// Main menu
int main() {
    int choice;
    loadPetsFromFile(); // Load pets from file at startup

    do {
        cout << "\n--- Pet Boarding System ---" << endl;
        cout << "1. Check-In Pet" << endl;
        cout << "2. Check-Out Pet" << endl;
        cout << "3. View All Pets" << endl;
        cout << "4. Search Pet by Name" << endl;
        cout << "5. Update Pet Data" << endl;
        cout << "6. Exit" << endl;
        cout << "Choose an option: ";
        cin >> choice;

        switch (choice) {
        case 1: checkInPet(); break;
        case 2: checkOutPet(); break;
        case 3: viewAllPets(); break;
        case 4: searchPetByName(); break; // Search query
        case 5: updatePetData(); break;   // Update query
        case 6: cout << "Exiting system..." << endl; break;
        default: cout << "Invalid choice. Please select again." << endl;
        }
    } while (choice != 6);

    // Clean up memory before exiting
    for (Pet* p : pets) {
        delete p;
    }

    return 0;
}