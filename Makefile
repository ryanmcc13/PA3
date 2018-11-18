FILES=$(addprefix bin/, $(addsuffix .class, BFS MyWikiRanker PageRank SpamFarm WeightedQ WikiCrawler Node Converter))

TEST=$(addprefix bin/, $(addsuffix .class, WeightedQTest PageRankTest SpamFarmTest WikiCrawlerTest))

FLAGS= -Xlint -Werror -d bin/

.PHONY: default makebin clean

default: $(FILES)
	@echo "compilation successful"

test: FLAGS:=$(FLAGS) -g
test: $(TEST) $(FILES)
	@echo "test compilation successful"

bin/%.class: src/%.java | makebin
	@echo "compiling $<"
	@javac $(FLAGS) -cp bin:src/ $<

bin/%.class: test/%.java | makebin
	@echo "compiling $<"
	@javac $(FLAGS) -cp bin:src/:test/ $<

clean:
	@[ ! -d bin ] || echo "removing bin directory"
	@[ ! -d bin ] || rm -r bin

makebin:
	@[  -d bin ] || echo "making bin directory"
	@[  -d bin ] || mkdir bin
