
## Overview of a sample XASDI application

In this application, 10 agents will send messages each other with unicast (10x10 messages in total) or broadcast (10 messages) style.

### Simulation steps is 4
- Step 0: Each agent sends an attribute message to the agent itself in order to change the attribute of the agent.
- Step 1: Each agent sends an individual message to the agent itself in order to output the attribute value.
- Step 2: The region sends broadcast messages to 10 agents (citizens).
- Step 3: Each agent sends mutual messages to all the agents in an N to N manner.

For details, please refer to the XASDI Application Development Guide.
