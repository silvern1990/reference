#include <Windows.h>
#include<stdio.h>
#include<string.h>


int main() {

	FILE* file = fopen("accept_call.lua", "rt");
	if (file == NULL) {
		return -1;	
	}
	char buffer[1024];
	memset(buffer, 0, sizeof(buffer));

	int multiByteSize = 0;
	int wideBufSize = 0;
	int originSize = 0;
	while (fgets(buffer, sizeof(buffer), file) != 0)
	{
		
		originSize = strlen(buffer);
		wideBufSize = MultiByteToWideChar(CP_UTF8, 0, buffer, originSize, NULL, 0);
		wchar_t* wideCharBuffer = (wchar_t*)malloc((wideBufSize + 1) * sizeof(wchar_t));
		wideCharBuffer[wideBufSize] = L'\0';

		MultiByteToWideChar(CP_UTF8, 0, buffer, originSize, wideCharBuffer, wideBufSize);

		multiByteSize = WideCharToMultiByte(20949, 0, wideCharBuffer, -1, NULL, 0, NULL, NULL);
		char* multiByteBuffer = (char*)malloc(multiByteSize * sizeof(char));
		
		WideCharToMultiByte(20949, 0, wideCharBuffer, -1, multiByteBuffer, multiByteSize, NULL, NULL);
		printf("%s\n", multiByteBuffer);
	}

	fclose(file);

	return 0;
}