# search.py
# ---------
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


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util
from game import Directions
from typing import List


class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem: SearchProblem) -> List[Directions]:
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]


from util import Stack, Queue, PriorityQueue


def depthFirstSearch(problem: SearchProblem) -> List[Directions]:
    """
    Search the deepest nodes in the search tree first (DFS).
    """
    stack = Stack()
    stack.push((problem.getStartState(), []))
    visited = set()

    while not stack.isEmpty():
        state, path = stack.pop()

        if state in visited:
            continue
        visited.add(state)

        if problem.isGoalState(state):
            return path

        for successor, action, _ in problem.getSuccessors(state):
            if successor not in visited:
                stack.push((successor, path + [action]))

    return []


def breadthFirstSearch(problem: SearchProblem) -> List[Directions]:
    """
    Search the shallowest nodes in the search tree first (BFS).
    """
    queue = Queue()
    queue.push((problem.getStartState(), []))
    visited = set()

    while not queue.isEmpty():
        state, path = queue.pop()

        if state in visited:
            continue
        visited.add(state)

        if problem.isGoalState(state):
            return path

        for successor, action, _ in problem.getSuccessors(state):
            if successor not in visited:
                queue.push((successor, path + [action]))
    return []


def uniformCostSearch(problem: SearchProblem) -> List[Directions]:
    """
    Search the node of least total cost first (UCS).
    """
    p_queue = PriorityQueue()
    p_queue.push((problem.getStartState(), []), 0)
    visited = {}

    while not p_queue.isEmpty():
        state, path = p_queue.pop()
        path_cost = problem.getCostOfActions(path)

        if state in visited and visited[state] <= path_cost:
            continue
        visited[state] = path_cost

        if problem.isGoalState(state):
            return path

        for successor, action, step_cost in problem.getSuccessors(state):
            new_path = path + [action]
            total_cost = problem.getCostOfActions(new_path)
            if successor not in visited or visited[successor] > total_cost:
                p_queue.push((successor, new_path), total_cost)
    return []


def nullHeuristic(state, problem=None):
    """
    A heuristic function that estimates the cost from the current state to the nearest
    goal in the provided SearchProblem. This heuristic returns zero for A* to behave
    like UCS when no other heuristic is provided.
    """
    return 0


def aStarSearch(problem: SearchProblem, heuristic=nullHeuristic) -> List[Directions]:
    """
    Search the node that has the lowest combined cost and heuristic first (A* search).
    """
    p_queue = PriorityQueue()
    start_state = problem.getStartState()
    p_queue.push((start_state, []), heuristic(start_state, problem))
    visited = {}

    while not p_queue.isEmpty():
        state, path = p_queue.pop()
        g_cost = problem.getCostOfActions(path)

        if state in visited and visited[state] <= g_cost:
            continue
        visited[state] = g_cost

        if problem.isGoalState(state):
            return path

        for successor, action, step_cost in problem.getSuccessors(state):
            new_path = path + [action]
            g_cost_new = problem.getCostOfActions(new_path)
            f_cost = g_cost_new + heuristic(successor, problem)

            if successor not in visited or visited[successor] > g_cost_new:
                p_queue.push((successor, new_path), f_cost)
    return []


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
