std::string md5(std::string str);

std::string decryptString(std::string str);
std::string encryptString(std::string str);
std::string decryptFile(const char *file);


std::string decryptString(std::string str, std::string key);
std::string encryptString(std::string originalString, std::string key);

void encryptFile(const char *originalPath, const char *encryptedFile, const char *key);
std::string decryptFile(const char *file, const char *key);

bool checkValidity(std::string userID, std::string license, std::string buildingID);
std::string getExpiredDate(std::string userID, std::string license, std::string buildingID);