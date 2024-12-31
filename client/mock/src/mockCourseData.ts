// mockCourseData.ts
export interface Course {
    name: string;
    code: string;
    professor: string;
    description: string;
    term: "Fall" | "Spring" | "Both";
    user: string;
}

export const mockCourses: Course[] = [
    {
        name: "The Digital World",
        code: "CSCI 0020",
        professor: "John Hughes",
        description: "Introduction to computing for humanities and social science students.",
        term: "Both",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computing Foundations: Data",
        code: "CSCI 0111",
        professor: "Barbara Lerner",
        description: "Introduction to computational thinking through data manipulation.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computing Foundations: Program Organization",
        code: "CSCI 0112",
        professor: "Kathi Fisler",
        description: "Introduction to program design and computer organization.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Introduction to Object-Oriented Programming",
        code: "CSCI 0150",
        professor: "Andy van Dam",
        description: "Introduction to programming and object-oriented concepts.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "CS: An Integrated Introduction",
        code: "CSCI 0170",
        professor: "Shriram Krishnamurthi",
        description: "Integrated view of computer science via a specific programming language.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Program Design with Data Structures",
        code: "CSCI 0200",
        professor: "John Jannotti",
        description: "Fundamental data structures and algorithms in software design.",
        term: "Both",
        user: "lily_smith@brown.edu"

    },
    {
        name: "Introduction to Discrete Structures",
        code: "CSCI 0220",
        professor: "Philip Klein",
        description: "Mathematical foundations of computer science.",
        term: "Both",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Fundamentals of Computer Systems",
        code: "CSCI 0300",
        professor: "Nick DeMarinis",
        description: "Covers fundamental concepts, principles, and abstractions that underlie the design and engineering of computer systems.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Software Engineering",
        code: "CSCI 0320",
        professor: "Tim Nelson",
        description: "Learn software engineering principles and practices through hands-on experience.",
        term: "Both",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Introduction to Computer Systems",
        code: "CSCI 0330",
        professor: "Tom Doeppner",
        description: "Understanding computer systems architecture and design.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Foundations of AI",
        code: "CSCI 0410",
        professor: "George Konidaris",
        description: "Basic concepts and techniques of artificial intelligence.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Data Structures, Algorithms, and Intractability: An Introduction",
        code: "CSCI 0500",
        professor: "Philip Klein",
        description: "Advanced algorithms and complexity theory.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Theory of Computation",
        code: "CSCI 1010",
        professor: "John Hughes",
        description: "Mathematical foundations of computation and computational complexity.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "The Basics of Cryptographic Systems",
        code: "CSCI 1040",
        professor: "Anna Lysyanskaya",
        description: "Introduction to cryptography and its mathematical foundations.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computer Graphics",
        code: "CSCI 1230",
        professor: "Daniel Ritchie",
        description: "Introduction to computer graphics algorithms and programming.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computer Graphics Lab",
        code: "CSCI 1234",
        professor: "Daniel Ritchie",
        description: "Practical implementation of computer graphics concepts.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Introduction to Computer Animation",
        code: "CSCI 1250",
        professor: "Barbara Meier",
        description: "Fundamentals of computer animation and motion.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Database Management Systems",
        code: "CSCI 1270",
        professor: "Stanley Zdonik",
        description: "Design and implementation of database systems.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Interaction Design",
        code: "CSCI 1300",
        professor: "Jeff Huang",
        description: "Principles of user interface and experience design.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Human Factors in Cybersecurity",
        code: "CSCI 1360",
        professor: "Jeff Huang",
        description: "Understanding human aspects of cybersecurity.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Distributed Systems",
        code: "CSCI 1380",
        professor: "Rodrigo Fonseca",
        description: "Principles of distributed computing systems.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Machine Learning",
        code: "CSCI 1420",
        professor: "Michael Littman",
        description: "Introduction to machine learning algorithms.",
        term: "Both",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computer Vision",
        code: "CSCI 1430",
        professor: "James Tompkin",
        description: "Algorithms and applications in computer vision.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Algorithmic Game Theory",
        code: "CSCI 1440",
        professor: "Amy Greenwald",
        description: "Intersection of game theory and computer science.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Deep Learning",
        code: "CSCI 1470",
        professor: "Daniel Ritchie",
        description: "Neural networks and deep learning architectures.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Design and Analysis of Algorithms",
        code: "CSCI 1570",
        professor: "Philip Klein",
        description: "Advanced algorithm design and analysis techniques.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computer Systems Security",
        code: "CSCI 1660",
        professor: "Roberto Tamassia",
        description: "Security principles and practices in computer systems.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Operating Systems",
        code: "CSCI 1670",
        professor: "Rodrigo Fonseca",
        description: "Operating system design and implementation.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computer Networks",
        code: "CSCI 1680",
        professor: "Theophilus Benson",
        description: "Computer network architecture and protocols.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "2D Game Engines",
        code: "CSCI 1950-N",
        professor: "John Jannotti",
        description: "Design and implementation of 2D game engines.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Data Science",
        code: "CSCI 1951-A",
        professor: "Paul Valiant",
        description: "Introduction to data science concepts and techniques.",
        term: "Both",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Blockchains & Cryptocurrencies",
        code: "CSCI 1951-L",
        professor: "Anna Lysyanskaya",
        description: "Technical foundations of blockchain systems.",
        term: "Spring",
        user: "lily_smith@brown.edu"
    },
    {
        name: "Computer Architecture",
        code: "CSCI 1952-Y",
        professor: "Maurice Herlihy",
        description: "Advanced computer architecture concepts.",
        term: "Spring",
        user: "lily_smith@brown.edu"

    },
    {
        name: "Accessible and Inclusive Cybersecurity",
        code: "CSCI 1953-A",
        professor: "Anna Lysyanskaya",
        description: "Making cybersecurity accessible to diverse populations.",
        term: "Fall",
        user: "lily_smith@brown.edu"
    }
];

export const userinput_courses = new Map<string, Course[]>();