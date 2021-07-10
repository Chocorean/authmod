.DEFAULT_GOAL=help
.PHONY: sonarqube build try clean prettier help
FORGE_VERSION=1.16.5-36.1.32
FORGE_JAR=forge-$(FORGE_VERSION).jar
branch := $(shell git branch --show-current)

sonarqube: ## Run sonarqube
	./gradlew sonarqube \
	-Dsonar.projectKey="authmod" \
	-Dsonar.organization="chocorean-sc" \
	-Dsonar.host.url="https://sonarcloud.io" \
	-Dsonar.branch.name="${branch}" \
	-Dsonar.login="${SONAR_TOKEN}"

clean:
	./gradlew clean
	rm -rf tmp

build: ## Build the mod
	./gradlew build

tmp/$(FORGE_JAR):
	mkdir -p tmp
	curl "https://maven.minecraftforge.net/net/minecraftforge/forge/$(FORGE_VERSION)/forge-$(FORGE_VERSION)-installer.jar" --output tmp/forge.jar
	java -jar tmp/forge.jar --installServer tmp
	rm tmp/forge.jar

tmp/mods/authmod.jar:
	mkdir -p $(dir $@)
	./gradlew build
	cp build/libs/* $@

try: tmp/$(FORGE_JAR) tmp/mods/authmod.jar
	cd tmp && java -Xmx1024M -Xms1024M -jar $(FORGE_JAR) nogui

prettier:
	npm init -y
	npm install prettier-plugin-java --dev
	npx prettier --print-width 130 --write "**/*.java"
	rm -rf package.json package-lock.json node_modules

help: ## Show this help
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
