# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        legalMoves = gameState.getLegalActions()

        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"

        food_list = newFood.asList()
        food_distance = [manhattanDistance(newPos, food) for food in food_list]
        ghost_dist = [manhattanDistance(newPos, ghost.getPosition()) for ghost in newGhostStates]
        safeghost_dist = min(ghost_dist) if ghost_dist else float('inf')
        scared_gost = [time for time in newScaredTimes if time>0]

        ghost_penalty = 0
        if safeghost_dist<2 and not scared_gost:
            return -float('inf')

        if food_distance:
            fscore = sum(1/dist for dist in food_distance) + 10 / (len(food_distance))
        else:
            return float('inf')

        bonus_for_scaredg = sum(1/(dist+1) for dist, time in zip(ghost_dist, newScaredTimes) if time>0)
        penalty = 0  # stop
        if action == Directions.STOP:
            penalty = 10

        add_score = fscore + bonus_for_scaredg - penalty - ghost_penalty
        return successorGameState.getScore() + add_score

def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        #util.raiseNotDefined()

        def minimax(state, depth, agent_ind):
            if depth == self.depth or state.isLose() or state.isWin():
                return self.evaluationFunction(state)

            num_agents = state.getNumAgents()
            if agent_ind == 0:
                return max_value(state, depth, agent_ind, num_agents)
            else:
                return min_value(state, depth, agent_ind, num_agents)

        def max_value(state, depth, agent_ind, num_agents):
            max_val = float("-inf")
            for action in state.getLegalActions(agent_ind):
                succ = state.generateSuccessor(agent_ind, action)
                value = minimax(succ, depth, (agent_ind + 1) % num_agents)
                if value > max_val:
                    max_val = value
                    best_action = action

            if depth == 0:
                return best_action

            return max_val

        def min_value(state, depth, agent_ind, num_agents):
            min_val = float("inf")
            for action in state.getLegalActions(agent_ind):
                successor = state.generateSuccessor(agent_ind, action)
                if agent_ind == num_agents - 1:
                    value = minimax(successor, depth + 1, 0)
                else:
                    value = minimax(successor, depth, agent_ind + 1)

                if value < min_val:
                    min_val = value

            return min_val

        return max_value(gameState, 0, 0, gameState.getNumAgents())



class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        #util.raiseNotDefined()

        def alphaBeta(state, depth, agent_ind, alpha, beta):
            if depth == 0 or state.isLose() or state.isWin():
                return self.evaluationFunction(state)

            if agent_ind == 0:
                return max_value(state,depth, alpha, beta)
            else:
                return min_value(state, depth, agent_ind, alpha, beta)

        def max_value(state, depth, alpha, beta):
            v = float("-inf")
            best = None

            legal_actions = state.getLegalActions(0)
            if not legal_actions:
                return self.evaluationFunction(state)

            for action in state.getLegalActions(0):
                succ = state.generateSuccessor(0, action)
                val = alphaBeta(succ, depth, 1, alpha, beta)
                if val > v:
                    v = val
                    best = action
                if v > beta:
                    return v
                alpha = max(alpha, v)

            if depth == self.depth:
                return best
            return v

        def min_value(state, depth, agent_ind, alpha, beta):
            v = float("inf")

            legal_actions = state.getLegalActions(agent_ind)
            if not legal_actions:
                return self.evaluationFunction(state)

            next = agent_ind + 1
            if agent_ind == state.getNumAgents() - 1:
                next = 0
                depth -= 1

            for action in state.getLegalActions(agent_ind):
                succ = state.generateSuccessor(agent_ind, action)
                val = alphaBeta(succ, depth, next, alpha, beta)
                v = min(v, val)
                if v < alpha:
                    return v
                beta = min(beta, v)
            return v

        return alphaBeta(gameState, self.depth, 0, float('-inf'), float('inf'))


class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        #util.raiseNotDefined()

        def evaluate(state, agent_ind, depth):
            legal_moves = state.getLegalActions(agent_ind)
            if not legal_moves:
                return self.evaluationFunction(state)

            num_actions = len(legal_moves)
            prob = 1/num_actions
            expected_score = 0

            for action in legal_moves:
                succ = state.generateSuccessor(agent_ind, action)
                if agent_ind == state.getNumAgents() - 1:
                    expected_score += prob * evaluate_max(succ, depth+1)
                else:
                    expected_score += prob * evaluate(succ, agent_ind + 1, depth)

            return expected_score


        def evaluate_max(state, depth):
            if depth == self.depth or state.isLose() or state.isWin():
                return self.evaluationFunction(state)

            legal_moves = state.getLegalActions(0)
            if not legal_moves:
                return self.evaluationFunction(state)

            max_score = float("-inf")
            for action in legal_moves:
                succ = state.generateSuccessor(0, action)
                score = evaluate(succ, 1, depth)
                max_score = max(max_score, score)
            return max_score

        best_action = None
        best_score = float("-inf")
        for action in gameState.getLegalActions(0):
            succ = gameState.generateSuccessor(0, action)
            score = evaluate(succ, 1, 0)
            if score > best_score:
                best_score = score
                best_action = action

        return best_action


def betterEvaluationFunction(currentGameState: GameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    #util.raiseNotDefined()

    pac_position = currentGameState.getPacmanPosition()
    food_positions = currentGameState.getFood().asList()
    capsules = currentGameState.getCapsules()
    ghost_states = currentGameState.getGhostStates()
    current_score = currentGameState.getScore()

    if currentGameState.isWin():
        return float("inf")
    if currentGameState.isLose():
        return float("-inf")

    closest_food_dist = min(manhattanDistance(pac_position, food) for food in food_positions) if food_positions else 1
    active_ghost_dists = [
        manhattanDistance(pac_position, ghost.getPosition())
        for ghost in ghost_states if ghost.scaredTimer == 0
    ]
    scared_ghost_dists = [
        manhattanDistance(pac_position, ghost.getPosition())
        for ghost in ghost_states if ghost.scaredTimer > 0
    ]

    ghost_penalty = -10/min(active_ghost_dists) if active_ghost_dists else 0
    scared_bonus = 50/(min(scared_ghost_dists)+1) if scared_ghost_dists else 0

    return (current_score + 10/closest_food_dist + ghost_penalty + scared_bonus - 25*len(capsules) - 4*len(food_positions)      )

# Abbreviation
better = betterEvaluationFunction
