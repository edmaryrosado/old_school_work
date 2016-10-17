/**
 =========================
 | Author: Edmary Rosado |
 | Course: COP 3502-0001 |
 | Program 5 Binary Trees|
 | Date:   03/26/2012    |
 =========================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>

/** ---------------------------------------- COMMAND CODES ------------------------------------------- */
#define joined 1 // JOINED COMMAND
#define found 2 // FOUND COMMAND
#define linked 3 // LINK COMMAND
#define unlinked 4 // UNLINK COMMAND
#define quit 5 // QUIT COMMAND
#define show 6 // SHOWCONNECTIONS COMMAND
#define print 7 // PRINTDISMEMBERS COMMAND
#define moved 8 // MOVEDEVICES COMMAND


/** ------------------------------------- PROGRAM NULL CHECKS ----------------------------------------- */
// Memory Allocation Null Pointer Check
#define nullCheck( ptr ); if( ptr == NULL ) { printf("\n\nFailure allocating memory! Check line %d! Terminating Program!\n", __LINE__); exit(EXIT_FAILURE); }

// File Null Pointer Check
#define fileValidation( ptr ); if( ptr == NULL ) { printf("\n\nFile not found! Check line %d! Terminating Program!\n", __LINE__); exit(EXIT_FAILURE); }

/**  -------------------- Typedef Structs for Macs, the Tree and Commands ----------------------------- */
typedef struct allowedMACs
{
    int linkedMAC;
    struct allowedMACs *next;
} links;

typedef struct tree_node
{
    int MAC;
    char firstName[21];
    char lastName[21];
    int broadcastRange;
    int x;
    int y;
    links *deviceLinks;
    int numLinks;
    struct tree_node *left;
    struct tree_node *right;
} BSTnode;


// Commands...yes Command Struct
typedef struct _CommandMenu
{
    char command[20];
    unsigned int val;
    struct TheCommandMenu *next;
} CommandMenu, *usrCommands;

/** ----------------------------------- BSTnode Function Prototypes -------------------------------- */
BSTnode* makeNode(FILE* fin);
BSTnode* insertNode(BSTnode *theRoot, BSTnode *usr);
BSTnode* user_info(BSTnode *usr, int num);
BSTnode* deleteNode(BSTnode* theRoot, int value);
BSTnode* node_parent(BSTnode *theRoot, BSTnode *node);
BSTnode* smallest(BSTnode *theRoot);
BSTnode* largest(BSTnode *theRoot);

int node_isLeaf(BSTnode *currNode);
int node_hasLeft(BSTnode *currNode);
int node_hasRight(BSTnode *currNode);
void node_moveDevices(BSTnode *usr, int sizeX, int sizeY);

/** ------------------------------------- MAC Link Function Prototypes ------------------------------- */
int getMAC(BSTnode *usr, char *fn, char *ln);
bool isLinked(BSTnode *theRoot, int macA, int macB);
void linkChange(BSTnode *theRoot, int macA, int macB, const int comCode);
links* insert_Link(links *head, int num);
links* delete_Link(links *head, int num);
bool device_in_range(BSTnode *dA, BSTnode *dB);

/** ------------------------------------- Printing Function Prototypes ------------------------------- */
BSTnode* masterCommand(FILE *fin, FILE *fout, BSTnode *theRoot, const int cX, const int cY,const int comCode);
void link_printOut(FILE *fout, BSTnode *theRoot, int MAC_Address);
void print_illegalMac(FILE *fout, int MAC_Address);
void list_inOrder(FILE *fout, BSTnode *usr);

/** ---------------------------------- Deleting/Freeing Function Prototypes -------------------------- */
void remove_Tree(BSTnode *theRoot);
void remove_Link(BSTnode *usr);
void clearAll(BSTnode *theRoot, int MAC_Address);

/** MAIN DRIVER PROGRAM */
int main()
{
    // BST
    BSTnode *theRoot = NULL;

    // Variables
    int i, j, chk; // Dummy Variables
    int processes; // Number of Processes
    int numForRandGen; // Number for Random Num Generator
    int sizeX, sizeY; // Plot Sizes

    // File Input Output Pointers
    FILE *fin, *fout;

    // Array of commands of type CommandMenu
    CommandMenu commands[] =
    {
        {"JOIN", 1},
        {"FINDMAC", 2},
        {"LINK", 3},
        {"UNLINK", 4},
        {"QUIT", 5},
        {"SHOWCONNECTIONS", 6},
        {"PRINTDISMEMBERS",7},
        {"MOVEDEVICES",8}
    };

    CommandMenu choice[20];

    //Open Both Files
    fin = fopen("dis_simulator.in","r");    // Input File
    fileValidation(fin); // Check input file

    fout = fopen("dis_simulator.out","w");    // Output File
    fileValidation(fout); // Check output file

    // Scan First Number
    fscanf(fin, "%d", &numForRandGen);

    // Initialize Generator
    srand(numForRandGen);

    // Retrieve sizeX, sizeY
    fscanf(fin, "%d %d", &sizeX, &sizeY);

    // Scan the number of processes
    fscanf(fin, "%d", &processes);

    // For the Number of Operations
    for(i=0; i < processes; i++)
    {
        // Get Current Process
        fscanf(fin, "%s", choice->command);

        for(chk = 0; chk<9; chk++)
        {
            if(strcmp(choice->command, commands[chk].command) == 0)
            {
                choice->val = commands[chk].val;
            }
        }
        // Switch Select for Processes Based on Value from Command[]
        switch(choice->val)
        {
        case 1:
            // JOIN COMMAND
            //printf("JOIN:\n");
            theRoot = masterCommand(fin, fout, theRoot, sizeX, sizeY, joined);
            break;
        case 2:
            // FINDMAC COMMAND
            //printf("FINDMAC:\n");
            masterCommand(fin, fout, theRoot, sizeX, sizeY,  found);
            break;
        case 3:
            // LINK COMMAND
            //printf("LINK: ");
            masterCommand(fin, fout, theRoot, sizeX, sizeY,  linked);
            break;
        case 4:
            // UNLINK COMMAND
            //printf("UNLINK:\n");
            masterCommand(fin, fout, theRoot, sizeX, sizeY,  unlinked);
            break;
        case 5:
            // QUIT COMMAND
            //printf("QUIT:\n");
            masterCommand(fin, fout, theRoot, sizeX, sizeY,  quit);
            break;
        case 6:
            // SHOW CONNECTIONS COMMAND
            //printf("SHOWCONNECTIONS:\n");
            masterCommand(fin, fout, theRoot, sizeX, sizeY,  show);
            break;
        case 7:
            //PRINT DIS MEMBERS COMMAND
            //printf("PRINTDISMEMBERS:\n");
            masterCommand(fin, fout, theRoot, sizeX, sizeY,  print);
            break;
        case 8:
            //MOVE DEVICES COMMAND
            masterCommand(fin, fout, theRoot, sizeX, sizeY,  moved);
            break;

        default:
					// Do nothing....
            break;
        }

    }

    theRoot = NULL; // set root to null

    //Close Input File
    fclose(fin);
    fclose(fout);

    // Successful Execution!
    exit(EXIT_SUCCESS);

}

/** ----------------------------------- BSTnode Function -------------------------------- */
/* Set up the Node Scanned from File */
BSTnode* makeNode(FILE* fin)
{
    BSTnode *usr;
    usr = (BSTnode*)malloc(sizeof(BSTnode));

		// Scan credentials for new node
    fscanf(fin, "%d %s %s %d %d %d", &usr->MAC, usr->firstName, usr->lastName,
           &usr->broadcastRange, &usr->x, &usr->y);

    usr->numLinks = 0;
    usr->deviceLinks = NULL;
    usr->left = NULL;
    usr->right = NULL;

    return usr;
}

/* Add the new node to the BST */
BSTnode* insertNode(BSTnode *theRoot, BSTnode *usr)
{
		// Check for null pointer
    if (theRoot == NULL)
        return usr;
    else
    {

        if (usr->MAC > theRoot->MAC)
        {

            if (theRoot->right != NULL)
                theRoot->right = insertNode(theRoot->right, usr); // Insert Node

            else
                theRoot->right = usr;
        }


        else
        {

            if (theRoot->left != NULL)
                theRoot->left = insertNode(theRoot->left, usr); // Insert Node

            else
                theRoot->left = usr;
        }

        return theRoot;
    }
}

// Get The User's Info
BSTnode* user_info(BSTnode *treePTR, int num)
{
    // Check for Empty Tree
    if (treePTR != NULL)
    {

        // Search Root For MAC
        if (treePTR->MAC == num)
            return treePTR;

        // Search left For MAC
        if (num < treePTR->MAC)
            return user_info(treePTR->left, num); // Return User Information

        // Search Right For MAC
        else
            return user_info(treePTR->right, num); // Return User Information

    }
    else
        return NULL; // Not Found
}

// Delete Node
BSTnode* deleteNode(BSTnode* theRoot, int num)
{
    BSTnode *usr, *tmp, *usrNode;
    BSTnode *p;
    int usrMAC;
    char fn[21];
    char ln[21];
    int plotX;
    int plotY;
    int usrRange;
    int nLinks;
    links *linkPtr;

    usr = user_info(theRoot, num);

    p = node_parent(theRoot, usr);

		// if the node is a leaf (has no children)
    if (node_isLeaf(usr))
    {

        if (p == NULL)
        {
            free(theRoot); // free the root
            return NULL;
        }

        if (num < p->MAC)
        {
            free(p->left);
            p->left = NULL;
        }

        else
        {
            free(p->right);
            p->right = NULL;
        }

        return theRoot;
    }

		// If the node has left child
    if (node_hasLeft(usr))
    {
        if (p == NULL)
        {
            usrNode = usr->left;
            free(usr); // free the pointer
            return usrNode;
        }

        if (num < p->MAC)
        {
            usrNode = p->left;
            p->left = p->left->left;
            free(usrNode); // free the ponter
        }

        else
        {
            usrNode = p->right;
            p->right = p->right->left;
            free(usrNode); // free the pointer
        }
        return theRoot; // Return the root of the tree after the deletion.
    }

		// If the node has right child
    if (node_hasRight(usr))
    {
        if (p == NULL)
        {
            usrNode = usr->right;
            free(usr); // free the pointer
            return usrNode;
        }

        if (num < p->MAC)
        {
            usrNode = p->left;
            p->left = p->left->right;
            free(usrNode); // free the pointer
        }

        else
        {
            usrNode = p->right;
            p->right = p->right->right;
            free(usrNode);
        }
        return theRoot;
    }


    tmp = smallest(usr->right); // Get the node with smallest value in tree

    usrMAC = tmp->MAC;
    strcpy(fn, tmp->firstName);
    strcpy(ln, tmp->lastName);
    usrRange = tmp->broadcastRange;
    plotX = tmp->x;
    plotY = tmp->y;
    nLinks = tmp->numLinks;
    linkPtr = tmp->deviceLinks;

    deleteNode(theRoot, usrMAC); // Delete Node

    usr->MAC = usrMAC;
    strcpy(usr->firstName, fn);
    strcpy(usr->lastName, ln);
    usr->broadcastRange = usrRange;
    usr->x = plotX;
    usr->y = plotY;
    usr->numLinks = nLinks;
    usr->deviceLinks = linkPtr;

    return theRoot;
}

// Retrieve Parent Node
BSTnode* node_parent(BSTnode *theRoot, BSTnode *node)
{

    if (theRoot == NULL || theRoot == node)
        return NULL;

    if (theRoot->left == node || theRoot->right == node)
        return theRoot;

    if (node->MAC < theRoot->MAC)
        return node_parent(theRoot->left, node);

    else if (node->MAC > theRoot->MAC)
        return node_parent(theRoot->right, node);

    return NULL;
}

/* Retrieve the Node with Smallest Value in Tree */
BSTnode* smallest(BSTnode *theRoot)
{
    if (theRoot->left == NULL)
        return theRoot;

    else
        return smallest(theRoot->left);
}

/* Retrieve the Node with Largest Value in Tree */
BSTnode* largest(BSTnode *theRoot)
{
    if (theRoot->right == NULL)
        return theRoot;

    else
        return largest(theRoot->right);
}

/* Check if the Current Node is Leaf (has no children) */
int node_isLeaf(BSTnode *currNode)
{
    return (currNode->left == NULL && currNode->right == NULL);
}

/* Return whether or not current Node Only has Left Child */
int node_hasLeft(BSTnode *currNode)
{
    return (currNode->left!= NULL && currNode->right == NULL);
}

/* Return whether or not current node only has right child */
int node_hasRight(BSTnode *currNode)
{
    return (currNode->left== NULL && currNode->right != NULL);
}

/* Move the Nodes Around */
void node_moveDevices(BSTnode *usr, int sizeX, int sizeY)
{
    if (usr != NULL)
    {
        node_moveDevices(usr->left, sizeX, sizeY);
        //printf("PRE XVAL = %d\n", usr->x);
        usr->x = rand() % sizeX;
        //printf("POST XVAL = %d\n", usr->x);
        usr->y = rand() % sizeY;
        node_moveDevices(usr->right, sizeX, sizeY);
    }
}

/** ------------------------------------- MAC Link Function ------------------------------- */

int getMAC(BSTnode *usr, char *fname, char *lname)
{
    if (usr != NULL)
    {

			// Searching for individual's MAC by name
        if ((strcmp(usr->lastName, lname)==0) && (strcmp(usr->firstName, fname)==0))
            return usr->MAC;


        if ((strcmp(usr->lastName, lname)==1) || ((strcmp(usr->lastName, lname)==0) && (strcmp(usr->firstName, fname)==1)))
            return getMAC(usr->left, fname, lname);


        else
            return getMAC(usr->right, fname, lname);

    }
    else
        return 0;
}

bool isLinked(BSTnode *theRoot, int macA, int macB)
{
    BSTnode *usrA, *usrB;
    links *lnkA, *lnkB;
    int macA_shared, macB_shared;

    usrA = user_info(theRoot, macA); // Retrieve User Info
    usrB = user_info(theRoot, macB); // Retrieve User Info
    lnkA = usrA->deviceLinks;
    lnkB = usrB->deviceLinks;

    while (lnkA != NULL)
    {
        if (lnkA->linkedMAC == macB)
        {
            macB_shared = 1;
        }
        lnkA = lnkA->next;
    }
    while (lnkB != NULL)
    {
        if (lnkB->linkedMAC == macA)
        {
            macA_shared = 1;
        }
        lnkB = lnkB->next;
    }
    if ((macA_shared==1) && (macB_shared==1))
        return true;
    else
        return false;
}

void linkChange(BSTnode *theRoot, int macA, int macB, const int comCode){
    BSTnode *usrA, *usrB;

    if(comCode==linked){
		usrA = user_info(theRoot, macA);
		usrB = user_info(theRoot, macB);
		usrA->deviceLinks = insert_Link(usrA->deviceLinks, macB);
		usrB->deviceLinks = insert_Link(usrB->deviceLinks, macA);

		usrA->numLinks++;
		usrB->numLinks++;
    }else if(comCode==unlinked){
		usrA = user_info(theRoot, macA); // Retrieve User Info
		usrB = user_info(theRoot, macB); // Retrieve User Info
		usrA->deviceLinks = delete_Link(usrA->deviceLinks, macB); // Delete Link
		usrB->deviceLinks = delete_Link(usrB->deviceLinks, macA); // Delete Link

		usrA->numLinks--; // Decrement number of links
		usrB->numLinks--; // Decrement number of links
    }
}

links* insert_Link(links *head, int num)
{
    links *lnkPtr;
    links *lnk;
    links *tmp;


    lnkPtr = (links*)malloc(sizeof(links));
    lnkPtr->linkedMAC = num;
    lnkPtr->next = NULL;


    if (head == NULL)
        return lnkPtr;

    if (num < head->linkedMAC)
    {
        lnkPtr->next = head;
        return lnkPtr;
    }


    lnk = head;


    while (lnk->next != NULL && lnk->next->linkedMAC < num){
			lnk = lnk->next;
	}


    tmp = lnk->next;


    lnk->next = lnkPtr;
    lnkPtr->next = tmp;


    return head;
}

	/* Delete A Link */
links* delete_Link(links *head, int num)
{
    links *lnkPtr, *lnk, *temp;
    lnkPtr = head;


    if (lnkPtr != NULL)
    {


        if (lnkPtr->linkedMAC == num)
        {
            temp = lnkPtr -> next;
            free(lnkPtr);
            return temp;
        }

			// List Traveral until node's next is null
        while (lnkPtr->next != NULL)
        {
            if (lnkPtr->next->linkedMAC == num)
            {
                lnk = lnkPtr -> next;
                lnkPtr->next = lnkPtr->next->next;
                free(lnk);
                return head;
            }
            lnkPtr = lnkPtr->next;
        }

    }
    return head; // always return head of list
}

	/* Return whether or not device is in range...*/
bool device_in_range(BSTnode *dA, BSTnode *dB)
{
		// The distance formula is similar to pythagorean thm
    int val1 = dA->x - dB->x;
    int val2 = dA->y - dB->y;
    int newLoc = (int)sqrt(((val1*val1)+(val2)*(val2)));
    if (newLoc <= dA->broadcastRange)
        return true;
    else
        return false;
}

/** ------------------------------------- Printing Function ------------------------------- */

void link_printOut(FILE *fout, BSTnode *theRoot, int MAC_Address)
{
    BSTnode *curr, *tmp;
    links *linkPtr;
    int sum = 0;

    curr = user_info(theRoot, MAC_Address);  // Retrieve User's Information
    linkPtr = curr->deviceLinks;

    if (curr->numLinks == 0)
        fprintf(fout,"MAC %d has no links.\n", MAC_Address);
    else
    {
        fprintf(fout,"Connections for MAC %d, %s %s, currently at position (%d, %d):\n", MAC_Address, curr->firstName, curr->lastName, curr->x, curr->y);
        fprintf(fout,"\tThere are a total of %d link(s).\n", curr->numLinks);
        while (linkPtr != NULL)
        {
            tmp = user_info(theRoot, linkPtr->linkedMAC);
            if (device_in_range(curr, tmp)) // If the device is in range, increment sum by 1
                sum++;
            linkPtr = linkPtr->next;
        }
        fprintf(fout,"\tThere are %d active link(s) within the broadcast range of %d:\n", sum, curr->broadcastRange);

        linkPtr = curr->deviceLinks;
        while (linkPtr != NULL)
        {
            tmp = user_info(theRoot, linkPtr->linkedMAC);
            if (device_in_range(curr, tmp)) // if the device is in range, do print
                fprintf(fout,"\t\tMAC %d, %s %s, currently at position (%d, %d)\n", tmp->MAC, tmp->firstName, tmp->lastName, tmp->x, tmp->y);
            linkPtr = linkPtr->next; // Keep traversing the list as long as linkPtr is not null
        }
    }
}

/* Master Command Controlling Function, calls functions and manipulates output file print out */
BSTnode* masterCommand(FILE *fin, FILE *fout, BSTnode *theRoot,const int cX,const int cY,const int comCode)
{
    int macA, macB;
    BSTnode *usr, *tmp;

		// Switching printouts and commands with command codes (#defines)
    switch(comCode)
    {
    case 1:
				// JOIN
			usr = makeNode(fin); // Scan in new node, first node scanned is root
        tmp = user_info(theRoot, usr->MAC);  // Retrieve User's Information
        if(tmp == NULL)
        {
            fprintf(fout,"%s %s, MAC %d, joined DIS.\n",
                   usr->firstName, usr->lastName, usr->MAC);


            return insertNode(theRoot, usr);
        }
        else
        {

            fprintf(fout,"Cannot Perform JOIN Command:\n");
            fprintf(fout,"\tMAC %d, %s %s - already a participant in the DIS program.\n",
                   usr->MAC, usr->firstName, usr->lastName);
        }
        break;

    case 2:
				// FOUND
        fscanf(fin, "%d", &macA);  // Scan in both MAC addresses
        usr = user_info(theRoot, macA); // Retrieve User's Information

        if (usr != NULL)
        {

            if (usr->numLinks == 1)
                fprintf(fout,"Found:  MAC %d, %s %s, currently at position (%d, %d), %d Link\n", usr->MAC, usr->firstName,
                       usr->lastName, usr->x, usr->y, usr->numLinks);
            else
                fprintf(fout,"Found:  MAC %d, %s %s, currently at position (%d, %d), %d Links\n", usr->MAC, usr->firstName,
                       usr->lastName, usr->x, usr->y, usr->numLinks);
        }

        else
            fprintf(fout,"MAC %d not found in the DIS system.\n", macA);

        break;

    case 3:
				// LINK
        fscanf(fin, "%d %d", &macA, &macB);  // Scan in both MAC addresses
        usr = user_info(theRoot, macA); // Retrieve User's Information
        tmp = user_info(theRoot, macB); // Retrieve User's Information


        if ((usr == NULL) || (tmp == NULL))
        {
            fprintf(fout,"Cannot Perform LINK Command:\n");

            if ((usr == NULL) && (tmp == NULL))
            {
				print_illegalMac(fout, macA);
				print_illegalMac(fout, macB);
            }

            else if ((usr == NULL) && (tmp != NULL))
					print_illegalMac(fout, macA);
            else
					print_illegalMac(fout, macB);
        }
        else
        {

				// connected = isLinked(theRoot, macA, macB);

            if (!isLinked(theRoot, macA, macB)) // If they are not linked
            {
					// Calling linkChange function with linked command code
                linkChange(theRoot, macA, macB, linked);
                fprintf(fout,"MAC %d and MAC %d are now linked.\n", macA, macB);
            }

            else
            {
					// They are already linked, we are good
                fprintf(fout,"Cannot Perform LINK Command:\n");
                fprintf(fout,"\tMAC %d and MAC %d are already linked.\n", macA, macB);
            }
        }


        break;

    case 4:
				// UNLINK
			fscanf(fin, "%d %d", &macA, &macB); // Scan in both MAC addresses
        usr = user_info(theRoot, macA); // Retrieve User's Information
        tmp = user_info(theRoot, macB); // Retrieve User's Information

        if ((usr == NULL) || (tmp == NULL))
        {
            fprintf(fout,"Cannot Perform UNLINK Command:\n");

            if ((usr == NULL) && (tmp == NULL))
            {
				print_illegalMac(fout, macA);
				print_illegalMac(fout, macB);

            }

            else if ((usr == NULL) && (tmp != NULL))
				print_illegalMac(fout, macA);
            else
					print_illegalMac(fout, macB);
        }

        else
        {

				//connected = isLinked(theRoot, macA, macB);

            if (isLinked(theRoot, macA, macB))
            {
					// Calling linkChange function with unlinked command code
                linkChange(theRoot, macA, macB, unlinked);
                fprintf(fout,"MAC %d and MAC %d are no longer linked.\n", macA, macB);
            }

            else
            {
                fprintf(fout,"Cannot Perform UNLINK Command:\n");
                fprintf(fout,"\tMAC %d and MAC %d are not currently linked.\n", macA, macB);
            }
        }

        break;

    case 5:
				// QUIT
        fscanf(fin, "%d", &macA);

			usr = user_info(theRoot, macA); // Retrieve User's Information

        if (usr == NULL)
        {
            fprintf(fout,"Cannot Perform QUIT Command:\n");
            fprintf(fout,"\tMAC %d not found in the DIS system.\n", macA);
        }

        else
        {
				// ClearAll Command Call
            clearAll(theRoot, macA);

            theRoot = deleteNode(theRoot, macA);
            fprintf(fout,"MAC %d has been removed from the DIS system.\n", macA);
        }

        break;

    case 6:
				// SHOWCONNECTIONS
        fscanf(fin, "%d", &macA);

			usr = user_info(theRoot, macA); // Retrieve User Info


        if(usr != NULL)
			link_printOut(fout, theRoot, macA); // Print Link
        else{
			fprintf(fout,"Cannot Perform SHOWCONNECTIONS Command:\n");
			print_illegalMac(fout, macA);

        }

        break;

    case 7:
				// PRINTDISMEMBERS
        if (theRoot != NULL)
        {
            fprintf(fout,"Members of DevWits Internet Share:\n");

            list_inOrder(fout, theRoot); // Print list in order
        }

        else
        {
            fprintf(fout,"Cannot Perform PRINTDISMEMBERS Command:\n");
            fprintf(fout,"\tThere are currently no participants of the DIS system.\n");
        }

        break;

    case 8:
				// MOVEDEVICES
        if (theRoot != NULL)
        {
            node_moveDevices(theRoot, cX, cY); // Move devices using x and y coordinates

            fprintf(fout,"All devices successfully moved.\n");
        }
        else
        {
            fprintf(fout,"Cannot Perform MOVEDEVICES Command:\n");
            fprintf(fout,"\tThere are currently no participants of the DIS system.\n");
        }

		break;

    default:
        break;
    }

	return theRoot; // Return the root

    // Free Pointers Always
    free(tmp);
    free(usr);
}

void print_illegalMac(FILE *fout, int MAC_Address){
	fprintf(fout,"\tMAC %d - This MAC Address is not in the DIS system.\n", MAC_Address);
}

/* LISTING TREE NODES IN ORDER */
void list_inOrder(FILE *fout, BSTnode *usr)
{
		// Check the Root for Null Val
    if (usr != NULL)
    {
        list_inOrder(fout, usr->left); // List in Order Recursive Call

        if (usr->numLinks == 1)
            fprintf(fout,"\tMAC %d, %s %s, currently at position (%d, %d), %d Link\n", usr->MAC, usr->firstName,
                   usr->lastName, usr->x, usr->y, usr->numLinks);
        else
            fprintf(fout,"\tMAC %d, %s %s, currently at position (%d, %d), %d Links\n", usr->MAC, usr->firstName,
                   usr->lastName, usr->x, usr->y, usr->numLinks);
        list_inOrder(fout, usr->right); // List iin Order Recursive Call
    }
}


/** ---------------------------------- Deleting/Freeing Function -------------------------- */
/* Total Tree Removal - while tree does not equal null, keeps recursively calling itself*/
void remove_Tree(BSTnode *theRoot)
{
    if(theRoot != NULL)
    {
        remove_Tree(theRoot->left); // remove left children
        remove_Tree(theRoot->right); // remove right children
        free(theRoot); // remove the root itself
    }
}

/* Multiple Link Removal - continues until user's deviceLinks is null*/
void remove_Link(BSTnode *usr)
{
    links *tmp;
    links *linkPtr = usr->deviceLinks;

    while (linkPtr != NULL)
    {
        tmp = linkPtr->next;
        free(linkPtr);
        linkPtr = tmp;
    }

    return;
}

/* Clears Out Everything - shared links and all */
void clearAll(BSTnode *theRoot, int MAC)
{
    BSTnode *usr, *tmpPtr;
    usr = user_info(theRoot, MAC); // Give usr theRoots information
    links *linkPtr = usr->deviceLinks; // Link pointer set to theRoot's deviceLinks list

		// Traverse while there are links
    while (linkPtr != NULL)
    {
        tmpPtr = user_info(theRoot, linkPtr->linkedMAC);
        tmpPtr->deviceLinks = delete_Link(tmpPtr->deviceLinks, MAC); // delete link
        tmpPtr->numLinks--;
        linkPtr = linkPtr->next;
    }

    // Shared Link Removal
    remove_Link(usr); // remove link
}
