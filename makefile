TAG         := 1.5.1

KOTLINC ?= kotlinc
JAR     ?= jar
JAVA    ?= java

BUILD_DIR   := build
SRC_DIR     := src/main/kotlin
RES_DIR     := src/main/resources
MAIN_CLASS  ?= com.distantbytes.tictacterminal.MainKt
JAR_NAME    := Tic-tac-terminal-$(TAG).jar
JAR_PATH    := $(BUILD_DIR)/$(JAR_NAME)

KT_FILES  := $(shell find $(SRC_DIR) -name '*.kt' 2>/dev/null)
RES_FILES := $(shell find $(RES_DIR) -type f 2>/dev/null)

.PHONY: all run clean

all: $(JAR_PATH)

$(JAR_PATH): $(KT_FILES) $(RES_FILES)
	@mkdir -p $(BUILD_DIR)
	$(KOTLINC) $(KT_FILES) -include-runtime -d $@
	$(JAR) ufe $@ $(MAIN_CLASS)
	@if [ -d "$(RES_DIR)" ]; then \
		cd $(RES_DIR) && $(JAR) uf $(CURDIR)/$@ .; \
	fi

run: all
	$(JAVA) -jar $(JAR_PATH)

clean:
	rm -rf $(BUILD_DIR)